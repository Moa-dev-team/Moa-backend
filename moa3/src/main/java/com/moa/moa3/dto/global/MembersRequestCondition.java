package com.moa.moa3.dto.global;

import com.moa.moa3.entity.member.profile.Category;
import lombok.Data;

import java.util.List;

@Data
public class MembersRequestCondition {
    List<Category> categories;
}
