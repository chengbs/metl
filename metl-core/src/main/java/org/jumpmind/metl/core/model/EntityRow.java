package org.jumpmind.metl.core.model;

import java.io.Serializable;
import java.util.Map;

import org.jumpmind.metl.core.runtime.EntityData;

public class EntityRow implements Serializable {

    private static final long serialVersionUID = 1L;
    
    String name;
    
    Map<String, String> data;
    
    public EntityRow() {
    }

    public EntityRow(String entityName, Map<String, String> row) {
        this.name = entityName;
        this.data = row;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String entityName) {
        this.name = entityName;
    }
    
    public Map<String, String> getData() {
        return data;
    }
    
    public void setData(Map<String, String> row) {
        this.data = row;
    }
    
    public EntityData toEntityData(Model model) {
        EntityData entityData = null;
        for (ModelEntity entity : model.getModelEntities()) {
            for (ModelAttrib attribute : entity.getModelAttributes()) {
                if (name.equals(entity.getName())  && data.containsKey(attribute.getName())) {
                    if (entityData == null) {
                        entityData = new EntityData();
                    }
                    String stringValue = data.get(attribute.getName());
                    entityData.put(attribute.getId(), stringValue);
                }
            }
        }
        return entityData;
    }   
}
