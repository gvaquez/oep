package mashin.oep.model.node;

import java.util.ArrayList;
import java.util.List;

import mashin.oep.Utils;
import mashin.oep.model.HPDLSerializable;
import mashin.oep.model.ModelElementWithSchema;
import mashin.oep.model.Workflow;
import mashin.oep.model.property.PointPropertyElement;
import mashin.oep.model.terminal.Terminal;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

public abstract class Node extends ModelElementWithSchema implements HPDLSerializable {
  
  public static final String PROP_CONNECTION_SOURCE  = "prop.node.connection.source";
  public static final String PROP_CONNECTION_TARGET  = "prop.node.connection.target";
  public static final String PROP_POS                = "prop.node.pos";
  
  private static IPropertyDescriptor[] NODE_PROPERTY_DESCRIPTORS;
  
  protected PointPropertyElement position;
  
  protected Workflow workflow;
  
  protected List<Terminal> terminals;
  
  /**
   * List of connections at which this node is a source
   */
  private List<Connection> sourceConnections;
  /**
   * List of connections at which this node is a target
   */
  private List<Connection> targetConnections;
  
  public Node(Workflow workflow) {
    this.workflow = workflow;
    this.position = new PointPropertyElement(PROP_POS, "Position");
    this.terminals = new ArrayList<Terminal>();
    this.sourceConnections = new ArrayList<Connection>();
    this.targetConnections = new ArrayList<Connection>();
  }
  
  @Override
  public IPropertyDescriptor[] getPropertyDescriptors() {
    if(NODE_PROPERTY_DESCRIPTORS == null) {
      NODE_PROPERTY_DESCRIPTORS = Utils.combine(position.getPropertyDescriptors());
    }
    return Utils.combine(super.getPropertyDescriptors(), NODE_PROPERTY_DESCRIPTORS);
  }
  
  @Override
  public void setPropertyValue(Object propertyName, Object propertyValue) {
    String propertyNameStr = (String) propertyName;
    switch(propertyNameStr) {
    default:
      if (position.hasId(propertyNameStr)) {
        Object oldValue = position.getValue(propertyNameStr);
        position.setValue(propertyNameStr, propertyValue);
        firePropertyChange(propertyNameStr, oldValue, propertyValue);
      } else {
        super.setPropertyValue(propertyName, propertyValue);
      }
    }
  }
  
  @Override
  public Object getPropertyValue(Object propertyName) {
    String propertyNameStr = (String) propertyName;
    switch(propertyNameStr) {
    default:
      if (position.hasId(propertyNameStr)) {
        return position.getValue(propertyNameStr);
      } else {
        return super.getPropertyValue(propertyName);
      }
    }
  }
  
  public Point getPosition() {
    return this.position.getAsPoint();
  }
  
  public void setPosition(Point point) {
    Point oldPosition = this.position.getAsPoint();
    this.position.setFromPoint(point);
    firePropertyChange(PROP_POS, oldPosition, point);
  }
  
  public List<Connection> getSourceConnections() {
    return this.sourceConnections;
  }
  
  public List<Connection> getTargetConnections() {
    return this.targetConnections;
  }
  
  public boolean addConnectionInitiate(Connection connection) {
    if (connection.getSource() == connection.getTarget()) {
      
      return false;
      
    } else if (connection.getSource() == this) {
      
      if (!canConnectTo(connection.getTarget())
          || !connection.getSourceTerminal().canAddConnection(connection)) {
        return false;
      }
      
      boolean success = sourceConnections.add(connection);
      if (success) {
        connection.getSourceTerminal().addConnectionUpdate(connection);
        firePropertyChange(PROP_CONNECTION_SOURCE, null, connection);
      }
      return success;
      
    } else if (connection.getTarget() == this) {
      
      if (!canConnectFrom(connection.getSource())
          || !connection.getTargetTerminal().canAddConnection(connection)) {
        return false;
      }
      
      boolean success = targetConnections.add(connection);
      if (success) {
        connection.getTargetTerminal().addConnectionUpdate(connection);
        firePropertyChange(PROP_CONNECTION_TARGET, null, connection);
      }
      return success;
      
    }
    
    return false;
  }
  
  public void addConnectionUpdate(Connection connection) {
    if (connection.getSource() == this) {
      boolean success = sourceConnections.add(connection);
      if (success) {
        firePropertyChange(PROP_CONNECTION_SOURCE, null, connection);
      }
    } else if (connection.getTarget() == this) {
      boolean success = targetConnections.add(connection);
      if (success) {
        firePropertyChange(PROP_CONNECTION_TARGET, null, connection);
      }
    }
  }
  
  public void removeConnectionInitiate(Connection connection) {
    if (connection.getSource() == this) {
      boolean success = sourceConnections.remove(connection);
      if (success) {
        connection.getSourceTerminal().removeConnectionUpdate(connection);
        firePropertyChange(PROP_CONNECTION_SOURCE, connection, null);
      }
    } else if (connection.getTarget() == this) {
      boolean success = targetConnections.remove(connection);
      if (success) {
        connection.getTargetTerminal().removeConnectionUpdate(connection);
        firePropertyChange(PROP_CONNECTION_TARGET, connection, null);
      }
    }
  }
  
  public void removeConnectionUpdate(Connection connection) {
    if (connection.getSource() == this) {
      boolean success = sourceConnections.remove(connection);
      if (success) {
        firePropertyChange(PROP_CONNECTION_SOURCE, connection, null);
      }
    } else if (connection.getTarget() == this) {
      boolean success = targetConnections.remove(connection);
      if (success) {
        firePropertyChange(PROP_CONNECTION_TARGET, connection, null);
      }
    }
  }
  
  public List<Terminal> getTerminals() {
    return new ArrayList<Terminal>(terminals);
  }
  
  public abstract boolean canConnectTo(Node target);
  public abstract boolean canConnectFrom(Node source);
  
}
