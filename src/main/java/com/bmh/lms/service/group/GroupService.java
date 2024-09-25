package com.bmh.lms.service.group;

import com.bmh.lms.model.Group;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    Group createGroup(Group group, String username);

    List<Group> getAllGroup();

    Optional<Group> getGroupById(String group_id);

    Group updateGroup(String group_id, Group updatedGroup, String username);

    Boolean deleteGroup(String group_id, String username);
}
