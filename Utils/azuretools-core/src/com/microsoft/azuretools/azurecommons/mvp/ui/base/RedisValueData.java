/**
 * Copyright (c) Microsoft Corporation
 * 
 * All rights reserved.
 * 
 * MIT License
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 * 
 * THE SOFTWARE IS PROVIDED *AS IS*, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.microsoft.azuretools.azurecommons.mvp.ui.base;

import java.util.ArrayList;

public class RedisValueData {

    private String[] columnName;
    private ArrayList<String[]> rowData;
    private int columnNum;
    private String keyType;

    /**
     * Constructor for RedisValueData class.
     * 
     * @param columnName
     *            the column name array for the table widget
     * @param rowData
     *            the data for each table row
     * @param keyType
     *            the Redis Cache's key type
     */
    public RedisValueData(String[] columnName, ArrayList<String[]> rowData, String keyType) {
        setColumnName(columnName);
        setRowData(rowData);
        setKeyType(keyType);
        setColumnNum(columnName.length);
    }

    public int getColumnNum() {
        return columnNum;
    }

    public void setColumnNum(int columnNum) {
        this.columnNum = columnNum;
    }

    public ArrayList<String[]> getRowData() {
        return rowData;
    }

    public void setRowData(ArrayList<String[]> columnData) {
        this.rowData = columnData;
    }

    public String[] getColumnName() {
        return columnName;
    }

    public void setColumnName(String[] columnName) {
        this.columnName = columnName;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String dataType) {
        this.keyType = dataType;
    }

}