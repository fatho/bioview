package de.zrho.bioview.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import de.zrho.bioview.model.Network;
import de.zrho.bioview.model.Reaction;

public class ReactionTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -1206047952085839969L;
	
	private Network<String, Double> network;
	private List<Reaction<String, Double>> reactions;
	
	public ReactionTableModel(Network<String, Double> network) {
		this.network = network;
		this.reactions = network.getReactions();
	}

	@Override
	public int getRowCount() {
		return reactions.size();
	}

	@Override
	public int getColumnCount() {
		return 3;
	}
	
	@Override
	public String getColumnName(int column) {
		return new String[] {"Name", "Reactant", "Product"} [column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Reaction<String, Double> reaction = reactions.get(rowIndex);
		switch (columnIndex) {
		case 0: return reaction.getName();
		case 1: return reaction.getReactant();
		case 2: return reaction.getProduct();
		default: return null;
		}
	}
	
}
