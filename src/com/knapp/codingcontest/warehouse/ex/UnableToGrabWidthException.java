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

import java.util.List;

import com.knapp.codingcontest.data.Location;
import com.knapp.codingcontest.data.Product;
import com.knapp.codingcontest.warehouse.Robot;

public class UnableToGrabWidthException extends AbstractWarehouseException {
  private static final long serialVersionUID = 1L;

  // ----------------------------------------------------------------------------

  public UnableToGrabWidthException(final Robot robot, final Location location, final List<Product> products) {
    super(robot + ": can not grab products smaller than already on robot (or about to be loaded)" //
        + " - #" + products.size() + " from " + location);
  }

  // ----------------------------------------------------------------------------
}
