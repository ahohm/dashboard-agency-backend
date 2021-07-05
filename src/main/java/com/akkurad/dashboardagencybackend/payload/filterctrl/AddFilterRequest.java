package com.akkurad.dashboardagencybackend.payload.filterctrl;

import com.akkurad.dashboardagencybackend.dto.Filter;
import com.akkurad.dashboardagencybackend.payload.Condition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class AddFilterRequest {

    private Filter filter;
    private Condition condition;
    private String name;
}
