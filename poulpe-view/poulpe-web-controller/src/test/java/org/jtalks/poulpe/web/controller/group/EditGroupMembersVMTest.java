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
package org.jtalks.poulpe.web.controller.group;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.model.entity.User;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.utils.ObjectCreator;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests for {@link EditGroupMembersVM}
 *
 * @author Vyacheslav Zhivaev
 *
 */
public class EditGroupMembersVMTest {

    // SUT
    private EditGroupMembersVM viewModel;

    private List<User> usersAvailable;

    private Group groupToEdit;

    @Mock
    private GroupService groupService;
    @Mock
    private UserService userService;
    @Mock
    private WindowManager windowManager;

    @BeforeMethod
    public void setUp() throws NotFoundException {
        MockitoAnnotations.initMocks(this);

        usersAvailable = ObjectCreator.getFakeUsers(50);
        // we are assert, that half of users already in group
        List<User> usersAlreadyInGroup = usersAvailable.subList(0, usersAvailable.size() / 2);
        groupToEdit = createGroupWithUsers(usersAlreadyInGroup);

        givenGroupExistInPersistent();
        givenAvailableUsersExist();

        doNothing().when(windowManager).open(anyString());

        viewModel = new EditGroupMembersVM(windowManager, groupService, userService, groupToEdit);
    }

    /**
     * Test method for {@link org.jtalks.poulpe.web.controller.group.EditGroupMembersVM#add()}.
     */
    @Test
    public void testAdd() {
        User selected = viewModel.getAvail().get(0);
        viewModel.setAvailSelected(selected);

        viewModel.add();

        assertTrue(viewModel.getExist().contains(selected));
        assertFalse(viewModel.getAvail().contains(selected));
    }

    /**
     * Test method for {@link org.jtalks.poulpe.web.controller.group.EditGroupMembersVM#addAll()}.
     */
    @Test
    public void testAddAll() {
        List<User> selected = viewModel.getAvail();

        viewModel.addAll();

        assertTrue(viewModel.getExist().containsAll(selected));
        assertTrue(viewModel.getAvail().isEmpty());
    }

    /**
     * Test method for {@link org.jtalks.poulpe.web.controller.group.EditGroupMembersVM#remove()}.
     */
    @Test
    public void testRemove() {
        User selected = viewModel.getExist().get(0);
        viewModel.setExistSelected(selected);

        viewModel.remove();

        assertFalse(viewModel.getExist().contains(selected));
        assertTrue(viewModel.getAvail().contains(selected));
    }

    /**
     * Test method for {@link org.jtalks.poulpe.web.controller.group.EditGroupMembersVM#removeAll()}.
     */
    @Test
    public void testRemoveAll() {
        List<User> selected = viewModel.getExist();

        viewModel.removeAll();

        assertTrue(viewModel.getExist().isEmpty());
        assertTrue(viewModel.getAvail().containsAll(selected));
    }

    /**
     * Test method for {@link org.jtalks.poulpe.web.controller.group.EditGroupMembersVM#save()}.
     */
    @Test
    public void testSave() {
        viewModel.save();

        verify(groupService).saveGroup(groupToEdit);
    }

    /**
     * Test method for {@link org.jtalks.poulpe.web.controller.group.EditGroupMembersVM#cancel()}.
     */
    @Test
    public void testCancel() {
        viewModel.cancel();

        vefiryNothingChanges();
    }

    private void vefiryNothingChanges() {
        verify(userService, never()).setPermanentBanStatus(anyCollectionOf(User.class), anyBoolean(), anyString());
        verify(userService, never()).setTemporaryBanStatus(anyCollectionOf(User.class), anyInt(), anyString());
        // TODO: why it's missing?
//        verify(userService, never()).updateLastLoginTime(any(User.class));
        verify(userService, never()).updateUser(any(User.class));

        verify(groupService, never()).saveGroup(any(Group.class));
        verify(groupService, never()).deleteGroup(any(Group.class));
    }

    private void givenGroupExistInPersistent() throws NotFoundException {
        when(groupService.get(anyLong())).thenReturn(groupToEdit);
    }

    private void givenAvailableUsersExist() {
        when(userService.getUsersByUsernameWord(anyString())).thenReturn(usersAvailable);
        when(userService.getAll()).thenReturn(usersAvailable);
    }

    private Group createGroupWithUsers(List<User> usersInGroup) {
        Group group = new Group(RandomStringUtils.randomAlphanumeric(10), RandomStringUtils.randomAlphanumeric(20));
        group.setUsers(usersInGroup);
        return group;
    }

}