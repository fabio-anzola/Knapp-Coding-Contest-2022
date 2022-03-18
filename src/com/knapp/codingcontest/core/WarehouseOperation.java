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

import com.knapp.codingcontest.data.Location;

@SuppressWarnings("boxing")
public abstract class WarehouseOperation {
  // ----------------------------------------------------------------------------

  public abstract String toResultString();

  // ----------------------------------------------------------------------------
  // ----------------------------------------------------------------------------

  public static class NextOrder extends WarehouseOperation {
    private final String toResultString;

    NextOrder() {
      toResultString = String.format("NextOrder;");
    }

    @Override
    public String toResultString() {
      return toResultString;
    }
  }

  public static class PullFrom extends WarehouseOperation {
    private final String toResultString;

    PullFrom(final Location location) {
      toResultString = String.format("PullFrom;%s;%d;%d;%d;", //
          location.getType(), location.getLevel(), location.getPosition(), 1);
    }

    @Override
    public String toResultString() {
      return toResultString;
    }
  }

  public static class PushTo extends WarehouseOperation {
    private final String toResultString;

    PushTo(final Location location) {
      toResultString = String.format("PushTo;%s;%d;%d;%d;", //
          location.getType(), location.getLevel(), location.getPosition(), 1);
    }

    @Override
    public String toResultString() {
      return toResultString;
    }
  }

  // ----------------------------------------------------------------------------
}
