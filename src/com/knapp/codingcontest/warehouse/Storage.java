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

import java.util.List;
import java.util.stream.Collectors;

import com.knapp.codingcontest.data.Location;
import com.knapp.codingcontest.data.WarehouseCharacteristics;
import com.knapp.codingcontest.warehouse.ex.NoSuchLocationException;

public class Storage {
  private final WarehouseCharacteristics c;
  private final Robot robot;
  private final List<List<Location>> locations;
  private final Location entryLocation;
  private final Location exitLocation;

  protected Storage(final WarehouseCharacteristics c, final Robot robot, final List<List<Location>> locations,
      final Location entryLocation, final Location exitLocation) {
    this.c = c;
    this.robot = robot;
    this.locations = locations;
    this.entryLocation = entryLocation;
    this.exitLocation = exitLocation;
  }

  public Robot getRobot() {
    return robot;
  }

  public List<Location> getAllLocations() {
    return locations.stream().flatMap(List::stream).collect(Collectors.toList());
  }

  public Location getEntryLocation() {
    return entryLocation;
  }

  public Location getExitLocation() {
    return exitLocation;
  }

  public Location getLocation(final int level, final int position) throws NoSuchLocationException {
    if (((level < 0) || (level >= c.getNumberOfLevels())) //
        || ((position < 0) || (position >= c.getNumberOfLocationsPerLevel()))) {
      throw new NoSuchLocationException(c, level, position);
    }
    return locations.get(level).get(position);
  }
}
