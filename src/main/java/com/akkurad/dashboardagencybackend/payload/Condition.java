package com.akkurad.dashboardagencybackend.payload;

import com.google.firebase.remoteconfig.TagColor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Condition {

    private String name;

    private String expression;

    private TagColor tagColor;

}
