package de.zrho.bioview.controller;

import java.io.File;

public class GenericFileFilter extends javax.swing.filechooser.FileFilter {

	private String[] extensions;
	private String description;
	
	public GenericFileFilter(String description, String... extensions) {
		this.extensions = extensions.clone();
		for(int i = 0; i < extensions.length; i++) {
			if(!extensions[i].startsWith("."))
				extensions[i] = "." + extensions[i];
		}
		this.description = description;
	}

	@Override
	public boolean accept(File f) {
		if(f.isDirectory())
			return true;
		String name = f.getPath();
		for(int i = 0; i < extensions.length; i++) {
			if(name.endsWith(extensions[i]))
				return true;
		}
		return false;
	}

	@Override
	public String getDescription() {
		return description;
	}

}
