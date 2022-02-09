package com.oracle.simulator.site.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * An entry item in the quantity/cost report.
 *
 * @author Joby
 *
 */
@AllArgsConstructor
@Getter
@ToString
public class ReportItem {
    private Integer quantity;
    private Integer cost;

}
