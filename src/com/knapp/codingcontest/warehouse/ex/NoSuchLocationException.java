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

import com.knapp.codingcontest.data.WarehouseCharacteristics;

public class NoSuchLocationException extends AbstractWarehouseException {
  private static final long serialVersionUID = 1L;

  // ----------------------------------------------------------------------------

  public NoSuchLocationException(final WarehouseCharacteristics c, final int level, final int position) {
    super("do not have a location with level/position=" + level + "/" + position //
        + " ([0-" + (c.getNumberOfLevels() - 1) + "]/[0-" + (c.getNumberOfLocationsPerLevel() - 1) + "])");
  }

  // ----------------------------------------------------------------------------
}
