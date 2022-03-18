/* -*- java -*-
# =========================================================================== #
#                                                                             #
#                         Copyright (C) KNAPP AG                              #
#                                                                             #
#       The copyright to the computer program(s) herein is the property       #
#       of Knapp.  The program(s) may be used   and/or copied only with       #
#       the  written permission of  Knapp  or in  accordance  with  the       #
#       terms and conditions stipulated in the agreement/contract under       #
#       which the program(s) have been supplied.                              #
#                                                                             #
# =========================================================================== #
*/

package com.knapp.codingcontest.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.knapp.codingcontest.data.Location;
import com.knapp.codingcontest.data.Order;
import com.knapp.codingcontest.data.Product;
import com.knapp.codingcontest.data.WarehouseCharacteristics;
import com.knapp.codingcontest.warehouse.Robot;
import com.knapp.codingcontest.warehouse.Storage;
import com.knapp.codingcontest.warehouse.Warehouse;
import com.knapp.codingcontest.warehouse.WarehouseCostFactors;
import com.knapp.codingcontest.warehouse.WarehouseInfo;
import com.knapp.codingcontest.warehouse.ex.CantPullFromExitException;
import com.knapp.codingcontest.warehouse.ex.CantPushToEntryException;
import com.knapp.codingcontest.warehouse.ex.IllegalProductForOrderException;
import com.knapp.codingcontest.warehouse.ex.InsufficentProductsException;
import com.knapp.codingcontest.warehouse.ex.LocationLengthExceededException;
import com.knapp.codingcontest.warehouse.ex.NoMoreOrdersException;
import com.knapp.codingcontest.warehouse.ex.NoSuchLocationException;
import com.knapp.codingcontest.warehouse.ex.OrderIncompleteException;
import com.knapp.codingcontest.warehouse.ex.ProductSurplusException;
import com.knapp.codingcontest.warehouse.ex.RobotLengthExeededException;
import com.knapp.codingcontest.warehouse.ex.UnableToGrabWidthException;

public class WarehouseInternal implements Warehouse {
  // ----------------------------------------------------------------------------

  private final InputDataInternal input;
  private final MyStorage storage;

  private final WarehouseCostFactors costFactors = new WarehouseInternal.CostFactors();

  private final List<WarehouseOperation> warehouseOperations = new ArrayList<>();
  public int moves;
  private long distanceLevel;
  private long distancePosition;
  private double distanceDirect;

  // ----------------------------------------------------------------------------

  public WarehouseInternal(final InputDataInternal input) {
    this.input = input;
    storage = new MyStorage(input.getWarehouseCharacteristics());
  }

  // ----------------------------------------------------------------------------

  @Override
  public boolean hasNextOrder() {
    return input.hasNextOrder();
  }

  @Override
  public Order nextOrder() throws OrderIncompleteException, NoMoreOrdersException {
    final Order nextOrder = input.nextOrder();
    add(new WarehouseOperation.NextOrder());
    return nextOrder;
  }

  // ............................................................................

  @Override
  public Storage getStorage() {
    return storage;
  }

  @Override
  public List<Product> getRemainingProductsAtEntry() {
    return Collections.unmodifiableList(input.productInqueue());
  }

  @Override
  public List<Order> getRemainingOrders() {
    return Collections.unmodifiableList(input.remainingOrders());
  }

  // ............................................................................

  @Override
  public WarehouseInfo getInfoSnapshot() {
    return new WarehouseInfoSnapshot(this);
  }

  @Override
  public WarehouseCostFactors getCostFactors() {
    return costFactors;
  }

  // ----------------------------------------------------------------------------
  // ----------------------------------------------------------------------------

  public Iterable<WarehouseOperation> result() {
    return warehouseOperations;
  }

  protected void add(final WarehouseOperation warehouseOperation) {
    warehouseOperations.add(warehouseOperation);
  }

  // ----------------------------------------------------------------------------
  // ----------------------------------------------------------------------------

  private static Robot createRobot(final WarehouseInternal wh, final int robotLength) {
    return wh.new MyRobot(robotLength);
  }

  private static List<List<Location>> createLocations(final WarehouseInternal wh, final WarehouseCharacteristics c) {
    final List<List<Location>> locations = new ArrayList<>(c.getNumberOfLevels());
    for (int l = 0; l < c.getNumberOfLevels(); l++) {
      final List<Location> levelLocations = new ArrayList<>(c.getNumberOfLocationsPerLevel());
      locations.add(levelLocations);
      for (int p = 0; p < c.getNumberOfLocationsPerLevel(); p++) {
        levelLocations.add(wh.new StorageLocation(l, p, c.getLocationLength()));
      }
    }
    return locations;
  }

  // ............................................................................

  private class MyStorage extends Storage {
    private MyStorage(final WarehouseCharacteristics c) {
      super(c, WarehouseInternal.createRobot(WarehouseInternal.this, c.getRobotLength()), //
          WarehouseInternal.createLocations(WarehouseInternal.this, c), //
          new EntryLocation(c.getEntryLevel(), c.getEntryPosition()), //
          new ExitLocation(c.getExitLevel(), c.getExitPosition()));
      try {
        ((MyRobot) getRobot()).setLocation(getLocation(0, 0));
      } catch (final NoSuchLocationException exception) {
      }
    }
  }

  private class MyRobot extends Robot {
    private MyRobot(final int robotLength) {
      super(robotLength);
    }

    void setLocation(final Location location) {
      currentLocation = location;
    }

    @Override
    public void pullFrom(final Location location) throws CantPullFromExitException, InsufficentProductsException,
        RobotLengthExeededException, UnableToGrabWidthException {
      super.pullFrom(location);
      add(new WarehouseOperation.PullFrom(location));
      updateMovements(currentLocation, location);
      setLocation(location);
    }

    @Override
    public void pushTo(final Location location) throws CantPushToEntryException, InsufficentProductsException,
        LocationLengthExceededException, UnableToGrabWidthException, ProductSurplusException, IllegalProductForOrderException {
      super.pushTo(location);
      add(new WarehouseOperation.PushTo(location));
      updateMovements(currentLocation, location);
      setLocation(location);
    }
  }

  private void updateMovements(final Location currentLocation, final Location location) {
    moves++;
    final int dl = Math.abs(location.getLevel() - currentLocation.getLevel());
    final int dp = Math.abs(location.getPosition() - currentLocation.getPosition());
    distanceLevel += dl;
    distancePosition += dp;
    distanceDirect += Math.sqrt((dl * dl) + (dp * dp));
  }

  private class EntryLocation extends Location {
    private EntryLocation(final int entryLevel, final int entryPosition) {
      super(Location.Type.Entry, entryLevel, entryPosition, 0);
    }

    @Override
    public String toString() {
      int count = 0;
      final Iterator<Product> it = input.productInqueue().iterator();
      for (int length = 0; it.hasNext() && (length < storage.getRobot().getLength());) {
        final Product p = it.next();
        length += p.getLength();
        count++;
      }
      return "EntryLocation[level=" + getLevel() + ", position=" + getPosition() + ", length=" + getLength() + "]" //
          + "{entry.products(lim(robot.length))=" + (count > 0 ? input.productInqueue().subList(0, count) : "<empty>") + "}";
    }

    @Override
    public void checkPull(final int numberOfProducts) throws InsufficentProductsException {
      if (numberOfProducts > input.productInqueue().size()) {
        throw new InsufficentProductsException(this, numberOfProducts, input.productInqueue().size());
      }
    }

    @Override
    public List<Product> getCurrentProducts() {
      return Collections.unmodifiableList(input.productInqueue());
    }

    @Override
    public List<Product> pull(final int numberOfProducts) throws InsufficentProductsException {
      checkPull(numberOfProducts);
      final List<Product> pulled = new ArrayList<>(numberOfProducts);
      for (int i = 0; i < numberOfProducts; i++) {
        pulled.add(input.productInqueue().remove(0));
      }
      return pulled;
    }

    @Override
    public void checkPush(final List<Product> products) throws CantPushToEntryException {
      throw new CantPushToEntryException(this, products);
    }

    @Override
    public void push(final List<Product> products) throws CantPushToEntryException {
      checkPush(products);
      throw new CantPushToEntryException(this, products);
    }
  }

  private class ExitLocation extends Location {
    private ExitLocation(final int exitLevel, final int exitPosition) {
      super(Location.Type.Exit, exitLevel, exitPosition, 0);
    }

    @Override
    public String toString() {
      return "ExitLocation[level=" + getLevel() + ", position=" + getPosition() + ", length=" + getLength() + "]" //
          + "{currentOrder=" + input.currentOrder + "}";
    }

    @Override
    public void checkPull(final int numberOfProducts) throws CantPullFromExitException {
      throw new CantPullFromExitException(this, numberOfProducts);
    }

    @Override
    public List<Product> pull(final int numberOfProducts) throws CantPullFromExitException {
      checkPull(numberOfProducts);
      throw new CantPullFromExitException(this, numberOfProducts);
    }

    @Override
    public void checkPush(final List<Product> products) throws IllegalProductForOrderException, ProductSurplusException {
      if (input.currentOrder == null) {
        throw new IllegalStateException("can't push() unless nextOrder() has been called");
      }
      checkProductsNeeded(input.currentOrder, products);
    }

    private void checkProductsNeeded(final InputDataInternal.MyOrder order, final List<Product> products)
        throws IllegalProductForOrderException, ProductSurplusException {
      final Map<String, int[]> requested = new HashMap<>();
      for (final Product p : products) {
        if (!order.requested.containsKey(p.getCode())) {
          throw new IllegalProductForOrderException(order, p);
        }
        final int[] pr = requested.computeIfAbsent(p.getCode(), k -> new int[] { order.requested.get(k)[0] });
        if (pr[0] <= 0) {
          throw new ProductSurplusException(order, p);
        }
        pr[0]--;
      }
    }

    @Override
    public void push(final List<Product> products) throws IllegalProductForOrderException, ProductSurplusException {
      checkPush(products);
      for (final Product p : products) {
        input.currentOrder.requested.get(p.getCode())[0]--;
      }
    }
  }

  private class StorageLocation extends Location {
    private StorageLocation(final int level, final int position, final int length) {
      super(Location.Type.Storage, level, position, length);
    }

    @Override
    public String toString() {
      return "StorageLocation[level=" + getLevel() + ", position=" + getPosition() //
          + ", length=" + getLength() + "(" + getRemainingLength() + ")" + "]" //
          + "{currentProducts=" + getCurrentProducts() + "}";
    }

    @Override
    public void checkPull(final int numberOfProducts) throws InsufficentProductsException {
      sublist(this, getCurrentProducts(), numberOfProducts); // called for side-effect
    }

    @Override
    public List<Product> pull(final int numberOfProducts) throws InsufficentProductsException {
      checkPull(numberOfProducts);
      final List<Product> pulled = new ArrayList<>(numberOfProducts);
      for (int i = 0; i < numberOfProducts; i++) {
        pulled.add(currentProducts.remove(0));
      }
      return pulled;
    }

    @Override
    public void checkPush(final List<Product> ps) throws LocationLengthExceededException {
      if (getRemainingLength() < totalLength(ps)) {
        throw new LocationLengthExceededException(this, ps);
      }
    }

    @Override
    public void push(final List<Product> products) throws LocationLengthExceededException {
      checkPush(products);
      for (final Product product : products) {
        currentProducts.addFirst(product);
      }
    }
  }

  // ----------------------------------------------------------------------------

  private List<Product> sublist(final Object owner, final List<Product> products, final int numberOfProducts)
      throws InsufficentProductsException {
    if (numberOfProducts > products.size()) {
      throw new InsufficentProductsException(owner, numberOfProducts, products.size());
    }
    return products.subList(0, numberOfProducts);
  }

  private int totalLength(final List<Product> ps) {
    return ps.stream().mapToInt(p -> p.getLength()).sum();
  }

  // ----------------------------------------------------------------------------
  // ----------------------------------------------------------------------------

  private static class WarehouseInfoSnapshot implements WarehouseInfo, Serializable {
    private static final long serialVersionUID = 1L;

    private final int unfinishedOrderCount;
    private final int unfinishedProductStillAtEntryCount;
    private final int unfinishedProductInStorageCount;
    private final long distanceLevel;
    private final long distancePosition;
    private final double distanceDirect;

    private final double unfinishedOrdersCost;
    private final double unfinishedProductsStillAtEntryCost;
    private final double unfinishedProductsInStorageCost;
    private final double distanceCost;
    private final double totalCost;

    // ............................................................................

    private WarehouseInfoSnapshot(final WarehouseInternal wh) {
      unfinishedOrderCount = wh.getRemainingOrders().size() + (isFinished(wh, wh.input.currentOrder) ? 0 : 1);

      final Map<String, int[]> unfinishedProducts = wh.getRemainingOrders().stream().flatMap(o -> o.getProducts().stream())
          .map(p -> p.getCode()).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream()
          .collect(Collectors.toMap(e -> e.getKey(), e -> new int[] { e.getValue().intValue() }));
      if (wh.input.currentOrder != null) {
        for (final Map.Entry<String, int[]> e : wh.input.currentOrder.requested.entrySet()) {
          if (e.getValue()[0] > 0) {
            unfinishedProducts.computeIfAbsent(e.getKey(), k -> new int[1])[0] += e.getValue()[0];
          }
        }
      }
      final Map<String, Long> storageProducts = wh.storage.getAllLocations().stream()
          .flatMap(l -> l.getCurrentProducts().stream()).map(p -> p.getCode())
          .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
      unfinishedProductStillAtEntryCount = (int) unfinishedProducts.entrySet().stream().mapToLong((e) -> {
        final long sc = Math.max(0, e.getValue()[0] - storageProducts.getOrDefault(e.getKey(), 0L).intValue());
        e.getValue()[0] -= sc;
        return sc;
      }).sum();
      unfinishedProductInStorageCount = unfinishedProducts.values().stream().mapToInt(v -> v[0]).sum();

      distanceLevel = wh.distanceLevel;
      distancePosition = wh.distancePosition;
      distanceDirect = wh.distanceDirect;

      // . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .

      final WarehouseCostFactors c = wh.getCostFactors();
      distanceCost = distanceDirect * c.getDistanceCosts();
      unfinishedOrdersCost = (unfinishedOrderCount > 0 ? c.getUnfinishedOrdersPenalty() : 0);
      unfinishedProductsInStorageCost = unfinishedProductInStorageCount * c.getUnfinishedProductInStorageCost();
      unfinishedProductsStillAtEntryCost = unfinishedProductStillAtEntryCount * c.getUnfinishedProductStillAtEntryCost();

      totalCost = distanceCost + unfinishedOrdersCost + unfinishedProductsInStorageCost + unfinishedProductsStillAtEntryCost;
    }

    // ............................................................................

    private boolean isFinished(final WarehouseInternal wh, final InputDataInternal.MyOrder currentOrder) {
      return ((currentOrder == null) || wh.input.currentOrder.isComplete());
    }

    // ............................................................................

    @Override
    public int getUnfinishedOrderCount() {
      return unfinishedOrderCount;
    }

    @Override
    public int getUnfinishedProductStillAtEntryCount() {
      return unfinishedProductStillAtEntryCount;
    }

    @Override
    public int getUnfinishedProductInStorageCount() {
      return unfinishedProductInStorageCount;
    }

    @Override
    public synchronized double getDistanceDirect() {
      return distanceDirect;
    }

    @Override
    public synchronized long getDistanceLevel() {
      return distanceLevel;
    }

    @Override
    public synchronized long getDistancePosition() {
      return distancePosition;
    }

    // ............................................................................

    @Override
    public double getDistanceCost() {
      return distanceCost;
    }

    @Override
    public double getUnfinishedOrdersCost() {
      return unfinishedOrdersCost;
    }

    @Override
    public double getUnfinishedProductsStillAtEntryCost() {
      return unfinishedProductsStillAtEntryCost;
    }

    @Override
    public double getUnfinishedProductsInStorageCost() {
      return unfinishedProductsInStorageCost;
    }

    @Override
    public double getTotalCost() {
      return totalCost;
    }
  }

  // ----------------------------------------------------------------------------

  public static class CostFactors implements WarehouseCostFactors {
    @Override
    public double getUnfinishedOrdersPenalty() {
      return 1_000_000.0;
    }

    @Override
    public double getUnfinishedProductStillAtEntryCost() {
      return 200.0;
    }

    @Override
    public double getUnfinishedProductInStorageCost() {
      return 100.0;
    }

    @Override
    public double getDistanceCosts() {
      return 0.1;
    }
  }
}
