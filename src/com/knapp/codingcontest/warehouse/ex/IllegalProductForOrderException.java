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

package com.knapp.codingcontest.warehouse.ex;

import com.knapp.codingcontest.data.Order;
import com.knapp.codingcontest.data.Product;

public class IllegalProductForOrderException extends AbstractWarehouseException {
  private static final long serialVersionUID = 1L;

  // ----------------------------------------------------------------------------

  public IllegalProductForOrderException(final Order order, final Product product) {
    super(order + " does not need any " + product);
  }

  // ----------------------------------------------------------------------------
}
