package uo.sdi.presentation.helper;

import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

public class BundleFactorie {
	public static ResourceBundle getMessagesBundle(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if(facesContext==null)
		{
			return null;
		}
		ResourceBundle bundle = facesContext.getApplication()
				.getResourceBundle(facesContext, "msgs");
		return bundle;
	}
}
