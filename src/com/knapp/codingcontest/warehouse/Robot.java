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

package com.knapp.codingcontest.warehouse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.knapp.codingcontest.data.Location;
import com.knapp.codingcontest.data.Product;
import com.knapp.codingcontest.warehouse.ex.CantPullFromExitException;
import com.knapp.codingcontest.warehouse.ex.CantPushToEntryException;
import com.knapp.codingcontest.warehouse.ex.IllegalProductForOrderException;
import com.knapp.codingcontest.warehouse.ex.InsufficentProductsException;
import com.knapp.codingcontest.warehouse.ex.LocationLengthExceededException;
import com.knapp.codingcontest.warehouse.ex.ProductSurplusException;
import com.knapp.codingcontest.warehouse.ex.RobotLengthExeededException;
import com.knapp.codingcontest.warehouse.ex.UnableToGrabWidthException;

public abstract class Robot {
  // ----------------------------------------------------------------------------

  private final int length;
  protected Location currentLocation;
  private final LinkedList<Product> currentProducts = new LinkedList<>();

  // ----------------------------------------------------------------------------

  protected Robot(final int length) {
    this.length = length;
  }

  // ----------------------------------------------------------------------------

  @Override
  public String toString() {
    return "Robot[length=" + length + " (" + getRemainingLength() + ")" //
        + ", level/position=" + currentLocation.getLevel() + "/" + currentLocation.getPosition() + "]" //
        + "{" + currentProducts + "}";
  }

  public int getLength() {
    return length;
  }

  public int getRemainingLength() {
    return length - totalLength(getCurrentProducts());
  }

  public int getCurrentMaxWidth() {
    return maxWidth(getCurrentProducts());
  }

  public Location getCurrentLocation() {
    return currentLocation;
  }

  public List<Product> getCurrentProducts() {
    return Collections.unmodifiableList(currentProducts);
  }

  public void pullFrom(final Location location)
      throws CantPullFromExitException, InsufficentProductsException, RobotLengthExeededException, UnableToGrabWidthException {
    checkNull("location", location);
    checkLoad(location);
    load(location.pull(1));
  }

  public void pushTo(final Location location) throws CantPushToEntryException, InsufficentProductsException,
      LocationLengthExceededException, UnableToGrabWidthException, ProductSurplusException, IllegalProductForOrderException {
    checkNull("location", location);
    checkStore(location);
    location.push(store(1));
  }

  // ----------------------------------------------------------------------------
  // ----------------------------------------------------------------------------

  private void checkNull(final String name, final Object value) {
    if (value == null) {
      throw new IllegalArgumentException(name + " must not be <null>");
    }
  }

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

  private int maxWidth(final List<Product> ps) {
    return ps.stream().mapToInt(p -> p.getWidth()).max().orElse(0);
  }

  // . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .

  private void checkLoad(final Location location)
      throws CantPullFromExitException, InsufficentProductsException, RobotLengthExeededException, UnableToGrabWidthException {
    location.checkPull(1);
    final List<Product> ps = sublist(location, location.getCurrentProducts(), 1);
    if (getRemainingLength() < totalLength(ps)) {
      throw new RobotLengthExeededException(this, ps);
    }
    checkGrabWidth(location, ps);
  }

  private void checkGrabWidth(final Location location, final List<Product> ps) throws UnableToGrabWidthException {
    int maxWidth = maxWidth(getCurrentProducts());
    for (final Product p : ps) {
      if (p.getWidth() < maxWidth) {
        throw new UnableToGrabWidthException(this, location, ps);
      }
      maxWidth = Math.max(maxWidth, p.getWidth());
    }
  }

  // . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .

  private void checkStore(final Location location) throws CantPushToEntryException, InsufficentProductsException,
      LocationLengthExceededException, IllegalProductForOrderException, ProductSurplusException {
    final List<Product> ps = sublist(this, currentProducts, 1);
    location.checkPush(ps);
  }

  // ............................................................................

  private void load(final List<Product> products) {
    for (final Product product : products) {
      currentProducts.addFirst(product);
    }
  }

  private List<Product> store(final int numberOfProducts) {
    final List<Product> pulled = new ArrayList<>(1);
    for (int i = 0; i < numberOfProducts; i++) {
      pulled.add(currentProducts.remove(0));
    }
    return pulled;
  }

  // ----------------------------------------------------------------------------
}
