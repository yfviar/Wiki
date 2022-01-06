package com.yfvia.wiki.mapper;

import com.yfvia.wiki.domain.Test;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TestMapper {
    List<Test> list();
}
