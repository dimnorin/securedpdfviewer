package org.jivesoftware.smack.packet.znannya;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.packet.IQ;

public class PropertiesCollection extends IQ {
	private List<Property> propsList = new ArrayList<Property>();
	
	public void add(Property str){
		propsList.add(str);
	}
	
	public List<Property> getProperties(){
		return propsList;
	}
	
	@Override
	public String getChildElementXML() {
		return null;
	}
	
	public class Property{
		private String name;
		private String value;
		
		public Property(){}
		
		public Property(String name, String value) {
			super();
			this.name = name;
			this.value = value;
		}

        @Override
        public String toString()
        {
          return name+"="+value;
        }

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
}
