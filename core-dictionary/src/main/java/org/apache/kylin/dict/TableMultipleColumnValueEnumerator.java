/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.kylin.dict;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.apache.kylin.source.IReadableTable;

import com.google.common.collect.Maps;

/**
 * Created by mingmwang. optimize to read multiple cols.
 */
public class TableMultipleColumnValueEnumerator implements IDictionaryValueEnumerator<Map<Integer, String>> {

    private IReadableTable.TableReader reader;
    private int[] colIndexes;
    private final int colMaxIndex;
    private Map<Integer, String> colValueMap = Maps.newHashMap();

    public TableMultipleColumnValueEnumerator(IReadableTable.TableReader reader, int[] colIndexes) {
        this.reader = reader;
        this.colIndexes = colIndexes;
        int tmpColMaxIndex = -1;
        for (int colIndex : colIndexes) {
            if (colIndex > tmpColMaxIndex) {
                tmpColMaxIndex = colIndex;
            }
        }
        this.colMaxIndex = tmpColMaxIndex;
    }

    @Override
    public boolean moveNext() throws IOException {
        if (reader.next()) {
            colValueMap.clear();
            String[] split = reader.getRow();
            if (split.length == 1 && colIndexes.length == 1) {
                colValueMap.put(colIndexes[0], split[0]);
            } else {
                // normal case
                if (split.length <= colMaxIndex) {
                    throw new ArrayIndexOutOfBoundsException("Column no. " + Arrays.asList(colIndexes)
                            + " not found, line split is " + Arrays.asList(split));
                }
                for (int i = 0; i < colIndexes.length; i++) {
                    colValueMap.put(colIndexes[i], split[colIndexes[i]]);
                }
            }
            return true;

        } else {
            colValueMap = null;
            return false;
        }
    }

    @Override
    public void close() throws IOException {
        if (reader != null)
            reader.close();
    }

    @Override
    public Map<Integer, String> current() {
        return colValueMap;
    }
}
