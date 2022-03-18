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

/**
 *
 */
public interface WarehouseInfo {
  // ----------------------------------------------------------------------------

  /**
   * @return number of unfinished orders
   */
  int getUnfinishedOrderCount();

  /**
   * @return number of unfinished product still at entry
   */
  int getUnfinishedProductStillAtEntryCount();

  /**
   * @return number of unfinished product in storage
   */
  int getUnfinishedProductInStorageCount();

  /**
   * @return current distance(s) of movements
   */
  double getDistanceDirect();

  long getDistanceLevel();

  long getDistancePosition();

  // ----------------------------------------------------------------------------

  /**
   * @return costs of current distance
   */
  double getDistanceCost();

  /**
   * @return costs of current unfinished orders
   */
  double getUnfinishedOrdersCost();

  /**
   * @return costs of unfinished products still at entry
   */
  double getUnfinishedProductsStillAtEntryCost();

  /**
   * @return costs of unfinished products in storage
   */
  double getUnfinishedProductsInStorageCost();

  /**
   * The total result used for ranking.
   *
   *   (Excludes time-based ranking factor)
   *
   * @return
   */
  double getTotalCost();

  // ----------------------------------------------------------------------------
}
