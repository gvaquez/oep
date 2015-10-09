package io.mashin.oep.model.terminal;

import io.mashin.oep.model.connection.WorkflowConnection;
import io.mashin.oep.model.node.Node;

public class FanInTerminal extends InputTerminal {

  public FanInTerminal(String label, Node holderNode) {
    super(label, holderNode);
  }

  @Override
  public boolean canAddConnection(WorkflowConnection connection) {
    if (!connection.getTargetNode().equals(holderNode)
        || hasConnection(connection)) {
      return false;
    }
    return true;
  }

}