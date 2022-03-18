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

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class InputData {
  // ----------------------------------------------------------------------------

  private static final String PATH_INPUT_DATA;
  static {
    try {
      PATH_INPUT_DATA = new File("./data").getCanonicalPath();
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  // ----------------------------------------------------------------------------

  private final String dataPath;

  private WarehouseCharacteristics warehouseCharacteristics;
  protected final Map<String, Product> products = new HashMap<>();
  protected final List<Product> productsAtEntry = new LinkedList<Product>();
  protected final List<Order> allOrders = new LinkedList<Order>();

  // ----------------------------------------------------------------------------

  public InputData() {
    this(InputData.PATH_INPUT_DATA);
  }

  public InputData(final String dataPath) {
    this.dataPath = dataPath;
  }

  // ----------------------------------------------------------------------------

  @Override
  public String toString() {
    return "InputData@" + dataPath; // + "[\n " + palletTypes + ",\n " + packets + "\n]";
  }

  // ----------------------------------------------------------------------------

  public void readData() throws IOException {
    readWarehouseCharacteristics();
    readProducts();
    readProductInqueue();
    readOrders();
  }

  // ----------------------------------------------------------------------------

  public WarehouseCharacteristics getWarehouseCharacteristics() {
    return warehouseCharacteristics;
  }

  public List<Product> getAllProductsAtEntry() {
    return Collections.unmodifiableList(new ArrayList<>(productsAtEntry));
  }

  public List<Order> getAllOrders() {
    return Collections.unmodifiableList(allOrders);
  }

  // ----------------------------------------------------------------------------
  // ----------------------------------------------------------------------------

  private void readWarehouseCharacteristics() throws IOException {
    final Reader fr = new FileReader(fullFileName("warehouse-characteristics.properties"));
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(fr);
      final Properties _warehouseCharacteristics = new Properties();
      _warehouseCharacteristics.load(reader);
      warehouseCharacteristics = new WarehouseCharacteristics(_warehouseCharacteristics);
    } finally {
      close(reader);
      close(fr);
    }
  }

  // ----------------------------------------------------------------------------

  private void readProducts() throws IOException {
    final Reader fr = new FileReader(fullFileName("products.csv"));
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(fr);
      for (String line = reader.readLine(); line != null; line = reader.readLine()) {
        line = line.trim();
        if ("".equals(line) || line.startsWith("#")) {
          continue;
        }
        // code;length;width;
        final String[] columns = splitCsv(line);
        final String code = columns[0];
        final int length = Integer.parseInt(columns[1]);
        final int width = Integer.parseInt(columns[2]);
        final Product product = new Product(code, length, width);
        products.put(product.getCode(), product);
      }
    } finally {
      close(reader);
      close(fr);
    }
  }

  // ----------------------------------------------------------------------------

  private void readProductInqueue() throws IOException {
    final Reader fr = new FileReader(fullFileName("product-inqueue.csv"));
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(fr);
      for (String line = reader.readLine(); line != null; line = reader.readLine()) {
        line = line.trim();
        if ("".equals(line) || line.startsWith("#")) {
          continue;
        }
        // code;
        final String[] columns = splitCsv(line);
        final String code = columns[0];
        final Product product = products.get(code);
        productsAtEntry.add(product);
      }
    } finally {
      close(reader);
      close(fr);
    }
  }

  // ----------------------------------------------------------------------------

  private void readOrders() throws IOException {
    final Reader fr = new FileReader(fullFileName("orders.csv"));
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(fr);
      for (String line = reader.readLine(); line != null; line = reader.readLine()) {
        line = line.trim();
        if ("".equals(line) || line.startsWith("#")) {
          continue;
        }
        // order-code;(product-code;requested-quantity;)+
        final String[] columns = splitCsv(line);
        final String code = columns[0];
        final List<Product> products = new ArrayList<>();
        for (int i = 1; i < columns.length; i++) {
          final String productCode = columns[i];
          products.add(this.products.get(productCode));
        }
        final Order order = new Order(code, products);
        allOrders.add(order);
      }
    } finally {
      close(reader);
      close(fr);
    }
  }

  // ----------------------------------------------------------------------------

  protected File fullFileName(final String fileName) {
    final String fullFileName = dataPath + File.separator + fileName;
    return new File(fullFileName);
  }

  protected void close(final Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (final IOException exception) {
        exception.printStackTrace(System.err);
      }
    }
  }

  // ----------------------------------------------------------------------------

  protected String[] splitCsv(final String line) {
    return line.split(";");
  }

  // ----------------------------------------------------------------------------
  // ............................................................................
}
