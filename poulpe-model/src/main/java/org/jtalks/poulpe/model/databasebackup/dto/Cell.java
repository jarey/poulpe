/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jtalks.poulpe.model.databasebackup.dto;

import org.jtalks.poulpe.model.databasebackup.SqlTypes;

/**
 * A one cell in the table's row. Every table has a number of rows where each row contains a number of cells.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public final class Cell {
    /**
     * Constructs a new cell object with provided column meta information and data object.
     * 
     * @param columnMetaInfo
     *            A column's meta information.
     * @param cellData
     *            Cell's data.
     * @throws NullPointerException
     *             If columnMetaInfo is null.
     */
    public Cell(final ColumnMetaData columnMetaInfo, final Object cellData) {
        super();
        if (columnMetaInfo == null) {
            throw new NullPointerException("columnMetaInfo cannot be null");
        }
        this.columnMetaInfo = columnMetaInfo;
        this.cellData = cellData;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 17;
        result = prime * result + ((cellData == null) ? 0 : cellData.hashCode());
        result = prime * result + ((columnMetaInfo.getName() == null) ? 0 : columnMetaInfo.getName().hashCode());
        result = prime * result + ((columnMetaInfo.getType() == null) ? 0 : columnMetaInfo.getType().hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        return (this == obj)
                || (obj instanceof Cell
                        && columnMetaInfo.getName() != null
                        && columnMetaInfo.getType() != null
                        && columnMetaInfo.getName().equals(((Cell) obj).columnMetaInfo.getName())
                        && columnMetaInfo.getType() == ((Cell) obj).columnMetaInfo.getType()

                        && ((cellData != null && cellData.equals(((Cell) obj).cellData))
                        || (cellData == ((Cell) obj).cellData)));
    }

    @Override
    public String toString() {
        return "Cell [ColumnMetaData=" + columnMetaInfo + ", cellData=" + cellData + "]";
    }

    /**
     * Return the column's name.
     * 
     * @return Column's name
     */
    public String getColumnName() {
        return columnMetaInfo.getName();
    }

    /**
     * Returns the SQL type of the column.
     * 
     * @return SQL type of the column.
     */
    public SqlTypes getSqlType() {
        return columnMetaInfo.getType();
    }

    /**
     * Return the column's data object.
     * 
     * @return Column's data
     */
    public Object getColumnData() {
        return cellData;
    }

    private final ColumnMetaData columnMetaInfo;
    private final Object cellData;
}
