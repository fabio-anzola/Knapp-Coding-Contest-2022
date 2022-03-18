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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.knapp.codingcontest.warehouse.ex.CantPullFromExitException;
import com.knapp.codingcontest.warehouse.ex.CantPushToEntryException;
import com.knapp.codingcontest.warehouse.ex.IllegalProductForOrderException;
import com.knapp.codingcontest.warehouse.ex.InsufficentProductsException;
import com.knapp.codingcontest.warehouse.ex.LocationLengthExceededException;
import com.knapp.codingcontest.warehouse.ex.ProductSurplusException;

public abstract class Location {
  // ----------------------------------------------------------------------------

  public static enum Type {
    Storage, Entry, Exit,;
  }

  // ----------------------------------------------------------------------------

  private final Location.Type type;
  private final int level;
  private final int position;
  private final int length;
  protected final LinkedList<Product> currentProducts = new LinkedList<>();

  // ----------------------------------------------------------------------------

  protected Location(final Location.Type type, final int level, final int position, final int length) {
    this.type = type;
    this.level = level;
    this.position = position;
    this.length = length;
  }

  // ----------------------------------------------------------------------------

  public Location.Type getType() {
    return type;
  }

  public int getLevel() {
    return level;
  }

  public int getPosition() {
    return position;
  }

  public int getLength() {
    return length;
  }

  public int getRemainingLength() {
    return length - getCurrentProducts().stream().mapToInt(p -> p.getLength()).sum();
  }

  public List<Product> getCurrentProducts() {
    return Collections.unmodifiableList(currentProducts);
  }

  // ----------------------------------------------------------------------------

  @Override
  public abstract String toString();

  // ----------------------------------------------------------------------------
  // ----------------------------------------------------------------------------

  public abstract void checkPull(int numberOfProducts) throws CantPullFromExitException, InsufficentProductsException;

  public abstract List<Product> pull(int numberOfProducts) throws CantPullFromExitException, InsufficentProductsException;

  public abstract void checkPush(List<Product> products) throws CantPushToEntryException, LocationLengthExceededException,
      IllegalProductForOrderException, ProductSurplusException;

  public abstract void push(List<Product> products) throws CantPushToEntryException, LocationLengthExceededException,
      IllegalProductForOrderException, ProductSurplusException;

  // ----------------------------------------------------------------------------
}
