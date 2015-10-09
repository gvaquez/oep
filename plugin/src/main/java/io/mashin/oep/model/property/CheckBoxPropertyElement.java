package io.mashin.oep.model.property;

import io.mashin.oep.model.property.filter.DefaultPropertyFilter;
import io.mashin.oep.model.property.filter.PropertyFilter;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class CheckBoxPropertyElement extends SingularPropertyElement {

  private IPropertyDescriptor propertyDescriptor;
  
  private boolean isChecked = false;
  
  public CheckBoxPropertyElement(String id, String name) {
    this(id, name, new DefaultPropertyFilter());
  }
  
  public CheckBoxPropertyElement(String id, String name, PropertyFilter filter) {
    super(id, name, filter);
  }

  @Override
  public IPropertyDescriptor getPropertyDescriptor() {
    if (propertyDescriptor == null) {
      propertyDescriptor = new CheckboxPropertyDescriptor(getId(), getName());
    }
    return propertyDescriptor;
  }

  public void setBooleanValue(boolean value) {
    isChecked = value;
  }
  
  public boolean getBooleanValue() {
    return isChecked;
  }
  
  @Override
  public void setValue(Object value) {
    isChecked = (Boolean) value;
  }

  @Override
  public void resetValue() {
    isChecked = false;
  }

  @Override
  public boolean isSet() {
    return true;
  }

  @Override
  public Object getValue() {
    return isChecked;
  }

  @Override
  public Object getEditableValue() {
    return this;
  }

  /**
   * A property descriptor for boolean values that uses the CheckboxCellEditor
   * 
   */
  public class CheckboxPropertyDescriptor extends PropertyDescriptor {
    
    /**
     * @param id
     * @param displayName
     */
    public CheckboxPropertyDescriptor(Object id, String displayName) {
      super(id, displayName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.properties.IPropertyDescriptor#createPr
     * opertyEditor(org.eclipse.swt.widgets.Composite)
     */
    public CellEditor createPropertyEditor(Composite parent) {
      CellEditor editor = new CheckboxCellEditor(parent, SWT.CHECK);
      if (getValidator() != null) {
        editor.setValidator(getValidator());
      }
      return editor;
    }

  }
  
}