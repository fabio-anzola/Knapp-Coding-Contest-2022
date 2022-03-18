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

package com.knapp.codingcontest.solution;

import java.util.Arrays;
import java.util.List;

import com.knapp.codingcontest.data.InputData;
import com.knapp.codingcontest.data.Institute;
import com.knapp.codingcontest.data.Location;
import com.knapp.codingcontest.data.Order;
import com.knapp.codingcontest.data.Product;
import com.knapp.codingcontest.warehouse.Robot;
import com.knapp.codingcontest.warehouse.Storage;
import com.knapp.codingcontest.warehouse.Warehouse;
import com.knapp.codingcontest.warehouse.WarehouseInfo;
import com.knapp.codingcontest.warehouse.ex.NoSuchLocationException;

/**
 * This is the code YOU have to provide
 */
public class Solution {
  public String getParticipantName() {
    return "Fabio Anzola";
  }

  public Institute getParticipantInstitution() {
    return Institute.HTL_Rennweg_Wien;
  }

  // ----------------------------------------------------------------------------

  protected final InputData input;
  protected final Warehouse warehouse;

  protected final Storage storage;
  protected final Location entryLocation;
  protected final Location exitLocation;
  protected final Robot robot;

  // ----------------------------------------------------------------------------

  Product[][] storage_anz;
  int x = 15;
  int y = 1000;

  public Solution(final Warehouse warehouse, final InputData input) {
    this.input = input;
    this.warehouse = warehouse;

    storage = warehouse.getStorage();
    entryLocation = storage.getEntryLocation();
    exitLocation = storage.getExitLocation();
    robot = storage.getRobot();
    storage_anz = new Product[x][y];

    // Check for place where product is stored then sort by shortest path!!!

    // TODO: prepare data structures
  }

  // ----------------------------------------------------------------------------

  /**
   * The main entry-point
   */
  public void run() throws Exception {
    while (warehouse.getRemainingProductsAtEntry().size() != 0) {
      robot.pullFrom(entryLocation);
      while (warehouse.getRemainingProductsAtEntry().size() > 2 && entryLocation.getCurrentProducts().get(0).getCode().equalsIgnoreCase(robot.getCurrentProducts().get(0).getCode()) && robot.getRemainingLength() >= entryLocation.getCurrentProducts().get(0).getLength()) {
        robot.pullFrom(entryLocation);
      }

      while (robot.getCurrentProducts().size() != 0) {
        int[] abc = nextStorageFree(robot.getCurrentProducts().get(0));
        storage_anz[abc[0]][abc[1]] = robot.getCurrentProducts().get(0);
        robot.pushTo(storage.getLocation(abc[0], abc[1]));
      }
    }
    //System.out.println(Arrays.deepToString( storage_anz));
    while (warehouse.hasNextOrder()) {
      Order o = warehouse.nextOrder();
      for (Product product : o.getProducts()) {
        int[] goTo = whereProduct(product.getCode());
        robot.pullFrom(storage.getLocation(goTo[0], goTo[1]));
        robot.pushTo(warehouse.getStorage().getExitLocation());
        if (storage.getLocation(goTo[0], goTo[1]).getCurrentProducts().size() == 0) {
          storage_anz[goTo[0]][goTo[1]] = null;
        }
      }
    }
  }

  public int[] nextStorageFree(Product p) {
    if (whereProduct(p.getCode()) != null) {
      try {
        if (storage.getLocation(whereProduct(p.getCode())[0], whereProduct(p.getCode())[1]).getRemainingLength() > p.getLength()) {
          return whereProduct(p.getCode());
        } else {
          for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
              Product product = storage_anz[j][i];
              if (product == null) {
                System.out.println("next loc: " + j + "   " + i);
                return new int[]{j, i};
              }
            }
          }
        }
      } catch (NoSuchLocationException e) {
        e.printStackTrace();
      }
    } else {
      for (int i = 0; i < y; i++) {
        for (int j = 0; j < x; j++) {
          Product product = storage_anz[j][i];
          if (product == null) {
            System.out.println("next loc: " + j + "   " + i);
            return new int[]{j, i};
          }
        }
      }
    }
    return null;
  }

  public int[] whereProduct(String code) {
    for (int i = 0, storage_anzLength = storage_anz.length; i < storage_anzLength; i++) {
      Product[] products = storage_anz[i];
      for (int j = 0, productsLength = products.length; j < productsLength; j++) {
        Product product = products[j];
        if (product != null) {
          if (product.getCode().equalsIgnoreCase(code)) {
            return new int[]{i, j};
          }
        }
      }
    }
    return null;
  }

  public int pathCost(int[] in, int[] product, int[] out) {
    if (in == null) {
      in = new int[]{this.storage.getEntryLocation().getLevel(), this.storage.getEntryLocation().getPosition()}; //x
    }
    if (out == null) {
      out = new int[]{this.storage.getExitLocation().getLevel(), this.storage.getExitLocation().getPosition()}; //y
    }

    int toMovement = (int) Math.sqrt(Math.abs(Math.pow(product[0] - in[0], 2)) + Math.abs(Math.pow(product[1] - in[1], 2)));
    int awayMovement = (int) Math.sqrt(Math.abs(Math.pow(out[0] - product[0], 2)) + Math.abs(Math.pow(out[1] - product[1], 2)));

    return toMovement + awayMovement;
  }

  // ----------------------------------------------------------------------------
  // ----------------------------------------------------------------------------

  /**
   * Just for documentation purposes.
   *
   * Method may be removed without any side-effects
   *
   *   divided into 4 sections
   *
   *     <li><em>input methods</em>
   *
   *     <li><em>main interaction methods</em>
   *         - these methods are the ones that make (explicit) changes to the warehouse
   *
   *     <li><em>information</em>
   *         - information you might need for your solution
   *
   *     <li><em>additional information</em>
   *         - various other infos: statistics, information about (current) costs, ...
   *
   */
  @SuppressWarnings("unused")
  private void apis() throws Exception {
    // ----- input -----

    final List<Product> allProductsAtEntry = input.getAllProductsAtEntry();
    final List<Order> allOrders = input.getAllOrders();

    final List<Product> remainingProducts = warehouse.getRemainingProductsAtEntry();
    final List<Order> remainingOrders = warehouse.getRemainingOrders();

    final Location location0 = storage.getLocation(0, 0);
    final List<Location> allLocations = storage.getAllLocations();

    // ----- main interaction methods -----

    Location location;

    location = entryLocation;
    robot.pullFrom(location);

    location = exitLocation;
    robot.pushTo(location);

    final Order order = warehouse.nextOrder();

    // ----- information -----

    final Product product = order.getProducts().get(0);

    location.getType();
    location.getLevel();
    location.getPosition();
    location.getLength();
    location.getRemainingLength();
    location.getCurrentProducts();

    final Location lamLocation = robot.getCurrentLocation();
    robot.getCurrentProducts();
    robot.getLength();
    robot.getRemainingLength();
    robot.getCurrentMaxWidth();

    // ----- additional information -----
    final WarehouseInfo info = warehouse.getInfoSnapshot();
  }

  // ----------------------------------------------------------------------------
}
