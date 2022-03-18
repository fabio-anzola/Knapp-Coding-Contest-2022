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

public interface WarehouseCostFactors {
  /**
   * @return costs if there are any unfinished order(s)
   */
  double getUnfinishedOrdersPenalty();

  /**
   * @return costs per unfinished product still at entry
   */
  double getUnfinishedProductStillAtEntryCost();

  /**
   * @return costs per unfinished product in storage
   */
  double getUnfinishedProductInStorageCost();

  /**
   * @return costs per distance-unit
   */
  double getDistanceCosts();
}
