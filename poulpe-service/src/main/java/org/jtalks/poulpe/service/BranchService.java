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

import org.jtalks.common.service.EntityService;
import org.jtalks.poulpe.model.dto.branches.BranchAccessChanges;
import org.jtalks.poulpe.model.dto.branches.BranchAccessList;
import org.jtalks.poulpe.model.entity.PoulpeBranch;

import java.util.List;

/**
 * @author Vitaliy Kravchenko
 * @author Kirill Afonin
 */
public interface BranchService extends EntityService<PoulpeBranch> {

    /**
     * Get list of all persistence objects T currently present in database.
     *
     * @return - list of persistence objects T.
     */
    List<PoulpeBranch> getAll();

    /**
     * Save or update branch.
     *
     * @param selectedBranch instance to save
     */
    void saveBranch(PoulpeBranch selectedBranch);

    /**
     * Removes all topics inside {@code victim} and then removes {@code victim} branch itself.
     *
     * @param victim the branch to be removed
     */
    void deleteBranchRecursively(PoulpeBranch victim);

    /**
     * Moves all topics inside {@code victim} to another {@code recipient} branch and then removes {@code victim}
     * branch.
     *
     * @param victim    the branch to be removed
     * @param recipient the branch to take topics of {@code victim}
     */
    void deleteBranchMovingTopics(PoulpeBranch victim, PoulpeBranch recipient);

    /**
     * Checks if the branch is duplicated.
     *
     * @param branch branch to check
     * @return true if branch is Duplicated
     */
    boolean isDuplicated(PoulpeBranch branch);

    /**
     * Return access list for branch
     *
     * @param branch branch which will be returned access list
     * @return access list
     */
    BranchAccessList getGroupAccessListFor(PoulpeBranch branch);

    /**
     * Change grants for branch
     *
     * @param branch  branch to which grants will be changed
     * @param changes grants for branch
     */
    void changeGrants(PoulpeBranch branch, BranchAccessChanges changes);

    /**
     * Change restriction for branch
     *
     * @param branch  branch to which restriction will be changed
     * @param changes new restriction for branch
     */
    void changeRestrictions(PoulpeBranch branch, BranchAccessChanges changes);
}