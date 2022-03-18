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

import java.util.List;

import com.knapp.codingcontest.data.Order;
import com.knapp.codingcontest.data.Product;
import com.knapp.codingcontest.warehouse.ex.NoMoreOrdersException;
import com.knapp.codingcontest.warehouse.ex.OrderIncompleteException;

public interface Warehouse {
  // ----------------------------------------------------------------------------
  // operations

  boolean hasNextOrder();

  Order nextOrder() throws OrderIncompleteException, NoMoreOrdersException;

  // ----------------------------------------------------------------------------
  // info

  Storage getStorage();

  List<Product> getRemainingProductsAtEntry();

  List<Order> getRemainingOrders();

  /**
   * Information about the current state of the warehouse.
   *
   * @return info
   */
  WarehouseInfo getInfoSnapshot();

  /**
   * Cost-factors used for calculating the result.
   *
   * @return cost-factors
   */
  WarehouseCostFactors getCostFactors();

  // ----------------------------------------------------------------------------
}
