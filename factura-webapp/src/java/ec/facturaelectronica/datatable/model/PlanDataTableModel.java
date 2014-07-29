/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.datatable.model;

import ec.facturaelectronica.model.Plan;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author Armando
 */
public class PlanDataTableModel extends ListDataModel<Plan> implements SelectableDataModel<Plan>{

    public PlanDataTableModel(List<Plan> data) {
        super(data);
    }
    
    @Override
    public Object getRowKey(Plan plan) {
        return plan.getId().toString();
    }

    @Override
    public Plan getRowData(String rowKey) {
        List<Plan> planes = (List<Plan>)getWrappedData();
        
        for (Plan plan : planes) {
            if(plan.getId().toString().equals(rowKey)){
                return plan;
            }
        }
        return null;
    }
    
}
