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

public class Product {
  private final String code;
  private final int length;
  private final int width;

  protected Product(final String code, final int length, final int width) {
    this.code = code;
    this.length = length;
    this.width = width;
  }

  public String getCode() {
    return code;
  }

  public int getLength() {
    return length;
  }

  public int getWidth() {
    return width;
  }

  @Override
  public String toString() {
    return "Product[code=" + code + ", length=" + length + ", width=" + width + "]";
  }
}
