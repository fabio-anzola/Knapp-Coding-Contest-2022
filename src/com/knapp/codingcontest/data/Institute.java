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

public enum Institute {
  // ----------------------------------------------------------------------------

  // UNIs, FHs & Sonstige
  FH_Campus_02, //
  FH_Joanneum, //
  FH_Technikum_Wien, //
  FH_Wr_Neustadt, //
  Montanuniversitaet, //
  TU_Graz, //
  TU_Wien, //
  Uni_Goettingen, //
  Universitaet_Klagenfurt, //
  Universitaet_Wien, //

  Sonstige, //

  // HTLs & Schulen
  Altes_Gymnasium_Leoben, //
  HTBLA_Kaindorf_Sulm, //
  HTBLuVA_Pinkafeld, //
  HTL_Bulme_Graz, //
  HTL_Rennweg_Wien, //
  HTL_Villach, //
  HTL_Weiz, //
  HTL_Wien_West, //
  TAC_Hartberg, //

  SonstigeSchule, //
  ;

  // ----------------------------------------------------------------------------

  public static Institute find(final String _institute) {
    return Institute.valueOf(_institute);
  }

  // ----------------------------------------------------------------------------
}
