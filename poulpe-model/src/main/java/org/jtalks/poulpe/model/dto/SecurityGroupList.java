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
package org.jtalks.poulpe.model.dto;

import org.jtalks.common.model.entity.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * While in all the places we work only with persistent groups, when it comes to security, we also have additional
 * 'groups' that do not exist as persisted unit - groups like Anonymous Users. It is impossible to add or edit members
 * of that group thus it doesn't make sense to create one, but we need to be able to set permissions for anonymous
 * users, thus we need to present them. <p/> This class is a list that can represent both persistent and 'special'
 * groups.
 *
 * @author stanislav bashkirtsev
 */
public class SecurityGroupList {
    private final List<Group> allGroups;

    public SecurityGroupList() {
        this(new ArrayList<Group>());
    }

    public SecurityGroupList(List<Group> allGroups) {
        this.allGroups = new ArrayList<Group>(allGroups);
    }

    public SecurityGroupList withAnonymousGroup() {
        if (!containsAnonymousGroup()) {
            allGroups.add(AnonymousGroup.ANONYMOUS_GROUP);
        }
        return this;
    }

    public Group removeAnonymousGroup() {
        if (allGroups.remove(AnonymousGroup.ANONYMOUS_GROUP)) {
            return AnonymousGroup.ANONYMOUS_GROUP;
        }
        return null;
    }

    public Group getAnonymousGroup() {
        if (containsAnonymousGroup()) {
            return AnonymousGroup.ANONYMOUS_GROUP;
        }
        return null;
    }

    public List<Group> getAllGroups() {
        return allGroups;
    }

    public boolean containsAnonymousGroup() {
        return allGroups.contains(AnonymousGroup.ANONYMOUS_GROUP);
    }
}