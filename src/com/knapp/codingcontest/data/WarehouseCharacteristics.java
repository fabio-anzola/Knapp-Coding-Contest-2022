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

import java.util.Properties;

public class WarehouseCharacteristics {
  // ----------------------------------------------------------------------------

  private final int numberOfLevels;
  private final int numberOfLocationsPerLevel;
  private final int locationLength;
  private final int locationMaxWidth;
  private final int robotLength;

  private final int entryLevel;
  private final int entryPosition;
  private final int exitLevel;
  private final int exitPosition;

  // ----------------------------------------------------------------------------

  WarehouseCharacteristics(final Properties warehouseCharacteristics) {
    numberOfLevels = Integer.parseInt((String) warehouseCharacteristics.get("numberOfLevels"));
    numberOfLocationsPerLevel = Integer.parseInt((String) warehouseCharacteristics.get("numberOfLocationsPerLevel"));
    locationLength = Integer.parseInt((String) warehouseCharacteristics.get("locationLength"));
    locationMaxWidth = Integer.parseInt((String) warehouseCharacteristics.get("locationMaxWidth"));
    robotLength = Integer.parseInt((String) warehouseCharacteristics.get("robotLength"));

    entryLevel = Integer.parseInt((String) warehouseCharacteristics.get("entryLevel"));
    entryPosition = Integer.parseInt((String) warehouseCharacteristics.get("entryPosition"));
    exitLevel = Integer.parseInt((String) warehouseCharacteristics.get("exitLevel"));
    exitPosition = Integer.parseInt((String) warehouseCharacteristics.get("exitPosition"));
  }

  // ----------------------------------------------------------------------------

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("WarehouseCharacteristics[\n");
    sb.append("    numberOfLevels             = ").append(numberOfLevels).append("\n");
    sb.append("    numberOfLocationsPerLevel  = ").append(numberOfLocationsPerLevel).append("\n");
    sb.append("    locationLength             = ").append(locationLength).append("\n");
    sb.append("    locationMaxWidth           = ").append(locationMaxWidth).append("\n");
    sb.append("    robotLength                  = ").append(robotLength).append("\n");
    sb.append("\n");
    sb.append("    entryLevel                 = ").append(entryLevel).append("\n");
    sb.append("    entryPosition              = ").append(entryPosition).append("\n");
    sb.append("    exitLevel                  = ").append(exitLevel).append("\n");
    sb.append("    exitPosition               = ").append(exitPosition).append("\n");
    sb.append("\n");
    sb.append("]");
    return sb.toString();
  }

  // ----------------------------------------------------------------------------

  public int getNumberOfLevels() {
    return numberOfLevels;
  }

  public int getNumberOfLocationsPerLevel() {
    return numberOfLocationsPerLevel;
  }

  public int getLocationLength() {
    return locationLength;
  }

  public int getLocationMaxWidth() {
    return locationMaxWidth;
  }

  public int getRobotLength() {
    return robotLength;
  }

  public int getEntryLevel() {
    return entryLevel;
  }

  public int getEntryPosition() {
    return entryPosition;
  }

  public int getExitLevel() {
    return exitLevel;
  }

  public int getExitPosition() {
    return exitPosition;
  }

  // ............................................................................

  // ----------------------------------------------------------------------------
}
