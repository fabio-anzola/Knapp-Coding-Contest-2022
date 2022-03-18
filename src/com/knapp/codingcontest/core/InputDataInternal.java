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

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.knapp.codingcontest.data.InputData;
import com.knapp.codingcontest.data.Order;
import com.knapp.codingcontest.data.Product;
import com.knapp.codingcontest.warehouse.ex.NoMoreOrdersException;
import com.knapp.codingcontest.warehouse.ex.OrderIncompleteException;

public class InputDataInternal extends InputData {
  // ----------------------------------------------------------------------------

  private final List<Product> productInqueue = new LinkedList<Product>();
  private final List<MyOrder> remainingOrders = new LinkedList<>();
  MyOrder currentOrder;

  // ----------------------------------------------------------------------------

  public InputDataInternal() {
  }

  public InputDataInternal(final String dataPath) {
    super(dataPath);
  }

  // ----------------------------------------------------------------------------

  @Override
  public void readData() throws IOException {
    super.readData();
    productInqueue.addAll(productsAtEntry);
    for (final Order o : allOrders) {
      remainingOrders.add(new MyOrder(o));
    }
  }

  // ----------------------------------------------------------------------------

  List<Product> productInqueue() {
    return productInqueue;
  }

  List<MyOrder> remainingOrders() {
    return remainingOrders;
  }

  boolean hasNextOrder() {
    return !remainingOrders.isEmpty();
  }

  Order nextOrder() throws OrderIncompleteException, NoMoreOrdersException {
    if ((currentOrder != null) && !currentOrder.isComplete()) {
      throw new OrderIncompleteException(currentOrder);
    }
    if (remainingOrders.isEmpty()) {
      throw new NoMoreOrdersException();
    }
    return currentOrder = remainingOrders.remove(0);
  }

  // ----------------------------------------------------------------------------
  // ----------------------------------------------------------------------------

  static class MyOrder extends Order {
    final Map<String, int[]> requested;

    private MyOrder(final Order order) {
      super(order.getCode(), order.getProducts());
      requested = new HashMap<>();
      for (final Product p : order.getProducts()) {
        if (!requested.containsKey(p.getCode())) {
          requested.put(p.getCode(), new int[] { 0, 0 });
        }
        requested.get(p.getCode())[0]++;
        requested.get(p.getCode())[1]++;
      }
    }

    boolean isComplete() {
      for (final int[] req : requested.values()) {
        if (req[0] > 0) {
          return false;
        }
      }
      return true;
    }

    @Override
    public String toString() {
      final StringBuilder sb = new StringBuilder(128);
      sb.append("MyOrder[code=").append(getCode()).append("]{");
      for (final Map.Entry<String, int[]> e : requested.entrySet()) {
        final int req = e.getValue()[1];
        final int rem = e.getValue()[0];
        final int prc = req - rem;
        sb.append(String.format("%s (%d/%d) ", e.getKey(), prc, req));
      }
      sb.append("}");
      return sb.toString();
    }
  }
}
