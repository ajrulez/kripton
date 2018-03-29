package com.abubusoft.kripton.quickstart.model;

import com.abubusoft.kripton.android.annotation.BindColumn;
import com.abubusoft.kripton.annotation.BindDisabled;
import com.abubusoft.kripton.annotation.BindType;

import java.util.List;

@BindType
public class Post {

    @BindColumn(foreignKey = User.class )
    public long userId;

    public long id;

    public String title;

    public String body;

}
