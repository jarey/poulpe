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
package org.jtalks.poulpe.service;

import java.util.List;

import org.jtalks.common.service.EntityService;
import org.jtalks.poulpe.model.entity.PoulpeGroup;

/**
 */
public interface GroupService extends EntityService<PoulpeGroup> {

    /**
     * Get list of all persistence objects T currently present in database.
     *
     * @return - list of persistence objects T.
     */
    List<PoulpeGroup> getAll();
    
    List<PoulpeGroup> getAllMatchedByName(String name);

    /**
     * Delete group
     * @param selectedBranch branch to delete
     */
    void deleteGroup(PoulpeGroup selectedGroup);

    /**
     * Save or update group.
     * @param selectedGroup instance to save
     * @throws NotUniqueException if group with the same name already exists
     */
    void saveGroup(PoulpeGroup selectedGroup);   
    

}