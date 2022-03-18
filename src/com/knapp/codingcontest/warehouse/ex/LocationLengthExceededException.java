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

public class LocationLengthExceededException extends AbstractWarehouseException {
  private static final long serialVersionUID = 1L;

  // ----------------------------------------------------------------------------

  public LocationLengthExceededException(final Location location, final List<Product> products) {
    super("can not load " + products + " - would exceed length of " + location);
  }

  // ----------------------------------------------------------------------------
}
