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
package org.jtalks.poulpe.web.controller.zkmacro;


import org.jtalks.poulpe.model.permissions.JtalksPermission;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * The page of editing branch permission consists of several blocks, each block represents a single permission and the
 * lists of groups that are granted to that permission (allowed) or restricted to it. The example of such block:
 * <pre>
 *  ____________________________________
 * |Create New Topics in the PoulpeBranch     |
 * |------------------------------------|
 * |Allowed: Moderators, Activated Users|
 * |Restricted: Banned Users            |
 * |____________________________________|
 * </pre>
 * There can be plenty of such blocks, they all look the same, but the permission, allowed & restricted groups are
 * always different. This class represents a model (data) for such block. Note, that each row of groups is represented
 * with {@link BranchPermissionRow}.
 *
 * @author stanislav bashkirtsev.
 * @see BranchPermissionRow
 * @see <a href="http://jtalks.org/display/jtalks/Managing+Permissions">Permission Management Vision</a>
 * @see <a href="http://jtalks.org/display/jtalks/Permission+Management">Permission Management Architecture</a>
 */
@Immutable
public final class BranchPermissionManagementBlock {
    /**
     * The permission this block represents (the block title on the page). Read class description for detailed
     * information.
     */
    private final JtalksPermission permission;
    private final BranchPermissionRow allowRow;
    private final BranchPermissionRow restrictRow;

    /**
     * Creates a block with permission and without any restricted/allowed groups. Note, that since the class is
     * immutable, you won't be able to set the allowed and restricted rows in the very same instance, for these purposes
     * you'll need to use {@link #setAllowRow(BranchPermissionRow)} & {@link
     * #setRestrictRow(BranchPermissionRow)} which create and return new instances each time. By default,
     * empty rows will be created for both allowed and restricted groups, this is the perfect case for brand new branch
     * where no groups were specified neither as restricted, nor as allowed.
     *
     * @param permission the permission this block represents
     */
    public BranchPermissionManagementBlock(@Nonnull JtalksPermission permission) {
        this(permission, BranchPermissionRow.newAllowRow(), BranchPermissionRow.newRestrictRow());
    }

    /**
     * Creates the whole instance with all the required information ready to feed the page.
     *
     * @param permission  the permission this block represents
     * @param allowRow    the set of groups that are granted to the specified {@code permission}
     * @param restrictRow the set of groups that are restricted (not allowed) to use the specified {@code permission}
     */
    public BranchPermissionManagementBlock(@Nonnull JtalksPermission permission,
                                           @Nonnull BranchPermissionRow allowRow,
                                           @Nonnull BranchPermissionRow restrictRow) {
        this.permission = permission;
        this.allowRow = allowRow;
        this.restrictRow = restrictRow;
    }

    /**
     * Gets the permission this block is all about.
     *
     * @return the permission this block is all about
     */
    public JtalksPermission getPermission() {
        return permission;
    }

    /**
     * Replaces the originally specified permission with the new instance.
     *
     * @param permission the new permission this block should refer to
     * @return new instance with new permission specified and others are kept previous
     */
    public BranchPermissionManagementBlock setPermission(@Nonnull JtalksPermission permission) {
        return new BranchPermissionManagementBlock(permission, allowRow, restrictRow);
    }

    /**
     * Gets the row that represents the granted groups (those that are allowed to do the action the {@link #permission}
     * is about).
     *
     * @return the row that represents the granted groups
     */
    public BranchPermissionRow getAllowRow() {
        return allowRow;
    }

    /**
     * Sets the row of groups that are granted to the {@link #permission}.
     *
     * @param allowRow the row of groups that are granted to the {@link #permission}
     * @return new instance with new argument specified and others are kept previous
     */
    public BranchPermissionManagementBlock setAllowRow(@Nonnull BranchPermissionRow allowRow) {
        return new BranchPermissionManagementBlock(permission, allowRow, restrictRow);
    }

    /**
     * Gets the row that represents the restricted groups (those that are not allowed to do the action the {@link
     * #permission} is about).
     *
     * @return the row that represents the restricted groups
     */
    public BranchPermissionRow getRestrictRow() {
        return restrictRow;
    }

    /**
     * Sets the row of groups that are restricted to execute the {@link #permission}.
     *
     * @param restrictRow the row of groups that are not allowed to fulfill the {@link #permission}
     * @return new instance with new argument specified and others are kept previous
     */
    public BranchPermissionManagementBlock setRestrictRow(@Nonnull BranchPermissionRow restrictRow) {
        return new BranchPermissionManagementBlock(permission, allowRow, restrictRow);
    }
}
