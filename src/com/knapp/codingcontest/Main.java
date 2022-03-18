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

package com.knapp.codingcontest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.knapp.codingcontest.core.InputDataInternal;
import com.knapp.codingcontest.core.PrepareUpload;
import com.knapp.codingcontest.core.WarehouseInternal;
import com.knapp.codingcontest.data.InputData;
import com.knapp.codingcontest.solution.Solution;
import com.knapp.codingcontest.warehouse.Warehouse;
import com.knapp.codingcontest.warehouse.WarehouseCostFactors;
import com.knapp.codingcontest.warehouse.WarehouseInfo;

/**
 * ----------------------------------------------------------------------------
 * you may change any code you like
 *   => but changing the output may lead to invalid results!
 * ----------------------------------------------------------------------------
 */
public class Main {
  // ----------------------------------------------------------------------------

  public static void main(final String... args) throws Exception {
    System.out.println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
    System.out.println("vvv   KNAPP Coding Contest #10: STARTING...    vvv");
    System.out.println(String.format("vvv                %s                    vvv", Main.DATE_FORMAT.format(new Date())));
    System.out.println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");

    System.out.println("");
    System.out.println("# ------------------------------------------------");
    System.out.println("# ... LOADING INPUT ...");
    final InputDataInternal iinput = new InputDataInternal();
    iinput.readData();
    final InputStat istat = new InputStat(iinput);

    System.out.println("");
    System.out.println("# ------------------------------------------------");
    System.out.println("# ... RUN YOUR SOLUTION ...");
    final long start = System.currentTimeMillis();
    final WarehouseInternal iwarehouse = new WarehouseInternal(iinput);
    final Warehouse warehouse = iwarehouse;
    final InputData input = iinput;
    final Solution solution = new Solution(warehouse, input);
    Throwable throwable = null;
    try {
      solution.run();
    } catch (final Throwable _throwable) {
      throwable = _throwable;
    }
    final long end = System.currentTimeMillis();
    System.out.println("");
    System.out.println("# ... DONE ... (" + Main.formatInterval(end - start) + ")");

    System.out.println("");
    System.out.println("# ------------------------------------------------");
    System.out.println("# ... RESULT/COSTS FOR YOUR SOLUTION ...");
    System.out.println("#     " + solution.getParticipantName() + " / " + solution.getParticipantInstitution());

    Main.printResults(istat, iwarehouse);

    System.out.println("");
    System.out.println("# ------------------------------------------------");
    System.out.println("# ... WRITING OUTPUT/RESULT ...");
    PrepareUpload.createZipFile(iwarehouse, solution);
    System.out.println("");
    System.out.println(">>> Created " + PrepareUpload.FILENAME_RESULT + " & " + PrepareUpload.FILENAME_UPLOAD_ZIP);
    System.out.println("");
    System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
    System.out.println("^^^   KNAPP Coding Contest #10: FINISHED       ^^^");
    System.out.println(String.format("^^^                %s                    ^^^", Main.DATE_FORMAT.format(new Date())));
    System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

    if (throwable != null) {
      System.out.println("");
      System.out.println("# ... Ooops ...");
      System.out.println("");
      throwable.printStackTrace(System.out);
    }
  }

  @SuppressWarnings("boxing")
  public static String formatInterval(final long interval) {
    final int h = (int) ((interval / (1000 * 60 * 60)) % 60);
    final int m = (int) ((interval / (1000 * 60)) % 60);
    final int s = (int) ((interval / 1000) % 60);
    final int ms = (int) (interval % 1000);
    return String.format("%02d:%02d:%02d.%03d", h, m, s, ms);
  }

  // ----------------------------------------------------------------------------
  // ----------------------------------------------------------------------------

  @SuppressWarnings("boxing")
  public static void printResults(final InputStat istat, final WarehouseInternal wh) throws Exception {
    //
    final WarehouseInfo info = wh.getInfoSnapshot();
    final WarehouseCostFactors c = wh.getCostFactors();

    final int uo = info.getUnfinishedOrderCount();
    final int upe = info.getUnfinishedProductStillAtEntryCount();
    final int ups = info.getUnfinishedProductInStorageCount();
    final long dl = info.getDistanceLevel();
    final long dp = info.getDistancePosition();
    final double dd = info.getDistanceDirect();

    final double c_d = info.getDistanceCost();
    final double c_t = info.getTotalCost();
    final double c_uo = info.getUnfinishedOrdersCost();
    final double c_upe = upe * c.getUnfinishedProductStillAtEntryCost();
    final double c_ups = ups * c.getUnfinishedProductInStorageCost();

    //
    final int ps = wh.getStorage().getAllLocations().stream().mapToInt(l -> l.getCurrentProducts().size()).sum();
    final double m_o = (double) wh.moves / istat.orders;
    final double m_p = (double) wh.moves / istat.productsOrders;
    final double d_o = dd / istat.orders;
    final double d_p = dd / istat.productsOrders;

    //
    System.out.println("");
    System.out.println(String.format("  --------------------------------------------------------------"));
    System.out.println(String.format("    INPUT STATISTICS                                            "));
    System.out.println(String.format("  ------------------------------------- : ----------------------"));
    System.out.println(String.format("      #orders                           :  %8d", istat.orders));
    System.out.println(String.format("      #products (order)                 :  %8d", istat.productsOrders));
    System.out.println(String.format("      #products (entry)                 :  %8d  (+%d)", //
        istat.productsInqueue, (istat.productsInqueue - istat.productsOrders)));
    System.out.println(String.format("      products / order                  :  %10.1f", istat.productsPerOrder));

    //
    System.out.println("");
    System.out.println(String.format("  --------------------------------------------------------------"));
    System.out.println(String.format("    RESULT STATISTICS                                           "));
    System.out.println(String.format("  ------------------------------------- : ----------------------"));
    System.out
        .println(String.format("      # products still at entry         :  %8d", wh.getRemainingProductsAtEntry().size()));
    System.out.println(String.format("      # products in storage             :  %8d", ps));
    System.out.println(String.format("      # moves                           :  %8d", wh.moves));
    System.out.println(String.format("      # moves / order                   :  %10.1f", m_o));
    System.out.println(String.format("      # moves / product (order)         :  %10.1f", m_p));
    System.out.println(String.format("      distance / order                  :  %10.1f", d_o));
    System.out.println(String.format("      distance / product (order)        :  %10.1f", d_p));

    //
    System.out.println("");
    System.out.println(String.format("  ============================================================================="));
    System.out.println(String.format("    RESULTS                                                                    "));
    System.out.println(String.format("  ===================================== : ============ | ======================"));
    System.out.println(String.format("      what                              :       costs  |  (details: count,...)"));
    System.out.println(String.format("  ------------------------------------- : ------------ | ----------------------"));
    System.out.println(String.format("   -> distance level                    :  %10s  |   %6d", "", dl));
    System.out.println(String.format("   -> distance position                 :  %10s  |   %6d", "", dp));
    System.out.println(String.format("   -> distance direct                   :  %10.1f  |   %8.1f", c_d, dd));
    System.out.println(String.format("   -> unfinished orders                 :  %10.1f  |   %6d", c_uo, uo));
    System.out.println(String.format("   -> unfinished products (at entry)    :  %10.1f  |   %6d", c_upe, upe));
    System.out.println(String.format("   -> unfinished products (in storage)  :  %10.1f  |   %6d", c_ups, ups));
    System.out.println(String.format("  ------------------------------------- : ------------ | ----------------------"));
    System.out.println("");
    System.out.println(String.format("   => TOTAL COST                           %10.1f", c_t));
    System.out.println(String.format("                                          ============"));
  }

  // ----------------------------------------------------------------------------

  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");

  // ----------------------------------------------------------------------------

  public static final class InputStat {
    public final int orders;
    public final int productsInqueue;
    public final int productsOrders;
    public final double productsPerOrder;

    public InputStat(final InputDataInternal iinput) {
      orders = iinput.getAllOrders().size();
      productsInqueue = iinput.getAllProductsAtEntry().size();
      productsOrders = iinput.getAllOrders().stream().mapToInt(o -> o.getProducts().size()).sum();
      productsPerOrder = (double) productsOrders / orders;
    }
  }
}
