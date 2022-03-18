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

package com.knapp.codingcontest.data;

import java.util.ArrayList;
import java.util.List;

public class Order {
  private final String code;
  private final List<Product> products;

  protected Order(final String code, final List<Product> products) {
    this.code = code;
    this.products = new ArrayList<>(products);
  }

  public String getCode() {
    return code;
  }

  public List<Product> getProducts() {
    return products;
  }

  @Override
  public String toString() {
    return "Order[code=" + code + "]{" + products + "}";
  }
}
