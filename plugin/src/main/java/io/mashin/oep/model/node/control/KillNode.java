/**
 * Copyright (c) 2015 Mashin (http://mashin.io). All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.mashin.oep.model.node.control;

import io.mashin.oep.hpdl.XMLReadUtils;
import io.mashin.oep.hpdl.XMLWriteUtils;
import io.mashin.oep.model.Workflow;
import io.mashin.oep.model.node.Node;
import io.mashin.oep.model.property.TextPropertyElement;
import io.mashin.oep.model.terminal.FanInTerminal;
import io.mashin.oep.model.terminal.NoOutputTerminal;

import org.dom4j.Element;

public class KillNode extends ControlNode {

  public static final String PROP_NODE_KILL_MESSAGE = "prop.node.kill.message";
  
  protected TextPropertyElement message;
  
  protected FanInTerminal fanInTerminal;
  protected NoOutputTerminal noOutputTerminal;
  
  public KillNode(Workflow workflow) {
    this(workflow, null);
  }

  public KillNode(Workflow workflow, org.dom4j.Node hpdlNode) {
    super(workflow, hpdlNode);
    fanInTerminal     = new FanInTerminal(TERMINAL_FANIN, this);
    noOutputTerminal  = new NoOutputTerminal(TERMINAL_NOOUT, this);
    terminals.add(fanInTerminal);
    terminals.add(noOutputTerminal);
    message = new TextPropertyElement(PROP_NODE_KILL_MESSAGE, "Message");
    addPropertyElement(message);
  }
  
  @Override
  public void initDefaults() {
    super.initDefaults();
    setName(workflow.nextId("kill"));
  }
  
  @Override
  public void write(org.dom4j.Element parentNode) {
    super.write(parentNode);
    
    Element element = (Element) hpdlModel.get();
    XMLWriteUtils.writeTextPropertyAsElement(message, element, "message");
  }

  @Override
  public void read(org.dom4j.Node hpdlNode) {
    super.read(hpdlNode);
    XMLReadUtils.initTextPropertyFrom(message, hpdlNode, "./message");
  }
  
  @Override
  public String getNodeType() {
    return TYPE_KILL;
  }
  
  public void setMessage(String message) {
    setPropertyValue(PROP_NODE_KILL_MESSAGE, message);
  }
  
  public String getMessage() {
    return message.getStringValue();
  }
  
  @Override
  public boolean canConnectTo(Node target) {
    return false;
  }

  @Override
  public boolean canConnectFrom(Node source) {
    return true;
  }

}
