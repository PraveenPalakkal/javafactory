package com.app.customer.controller;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.UIManager;

import java.awt.Font;

import javax.swing.JTable;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.SwingConstants;

import java.awt.SystemColor;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.app.customer.vo.Customer;
import com.app.framework.delegate.HJSFBusinessDelegate;
import com.app.framework.exception.HexApplicationException;
import com.hexaware.framework.logger.LogFactory;
import com.hexaware.framework.logger.Logger;
${Cache_import}

public class CustomerList extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static boolean flag = ${AuthenticationFlagset};
	private JPanel contentPane;
	private JPanel panel;
	private JPanel panel_1;
	private JLabel label;
	private JLabel label_1;
	private JLabel lblCustomerlist;
	private JTextField fieldValue;
	private JButton btnSearch;
	private JButton btnCustomerlist;
	private JTableHeader Header;
	public JComboBox<Object> comboBox;
	private JScrollPane scrollPane;
	private JTable table;
	private JButton btnClear;
	private JButton btnAdd;
	private JButton btnEmail;
	JPopupMenu popupMenu;
	JMenuItem menuItemEdit;
	JMenuItem menuItemDelete;
	private Vector rowIndex;
	private Vector rowIndex1;
	private static DefaultTableModel tableModel;
	${emailObjectCreation}
	CustomerAdd customerAdd;
	List<Customer> customerList;

	static Logger log = LogFactory.getLogger(CustomerList.class);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {

				try {
					if(flag)
					{
						CustomerList frame = new CustomerList();
						frame.setVisible(true);
					}
					${loginPageNavigation}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @throws HexApplicationException
	 */
	public CustomerList() throws HexApplicationException {
		start();
	}

	/*
	 * Call all setting function.
	 */
	public void start() throws HexApplicationException {
		panelTableSetting();
		labelSetting();
		buttonSetting();
		textFieldSetting();
		comboBoxSetting();
	}

	/*
	 * Panel and Table setting function.
	 */
	public void panelTableSetting() throws HexApplicationException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 760, 600);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);

		panel = new JPanel();
		panel.setBounds(0, 0, 156, 562);
		panel.setBorder(BorderFactory.createLineBorder(Color.black));
		contentPane.add(panel);
		panel.setLayout(null);

		panel_1 = new JPanel();
		panel_1.setBackground(SystemColor.control);
		panel_1.setForeground(Color.BLACK);
		panel_1.setBounds(155, 0, 589, 562);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		panel_1.setBorder(BorderFactory.createLineBorder(Color.black));

		scrollPane = new JScrollPane();
		panel_1.add(scrollPane);
		scrollPane.setBounds(10, 155, 569, 222);
		table = new javax.swing.JTable();
		table.setRowSelectionAllowed(true);
		tableModel = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		DisplayData(initialiseList());

		scrollPane.setViewportView(table);
		table.setBackground(SystemColor.control);
		table.setToolTipText("");
		table.setBorder(new LineBorder(new Color(0, 0, 0)));
		Color headerColor = new Color(244, 164, 96);
		Header = table.getTableHeader();
		Header.setBackground(headerColor);
		table.setFillsViewportHeight(true);
		table.add(table.getTableHeader(), BorderLayout.PAGE_START);
		/*
		 * Adding PopupMenu to table.
		 */
		JPopupMenu popupMenu = new JPopupMenu();
		final JMenuItem editItem = new JMenuItem("Edit");
		final JMenuItem deleteItem = new JMenuItem("Delete");
		popupMenu.add(editItem);
		popupMenu.add(deleteItem);
		table.setComponentPopupMenu(popupMenu);
		editItem.addMouseListener(new java.awt.event.MouseListener() {

			@Override
			public void mouseReleased(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mousePressed(java.awt.event.MouseEvent e) {
				int[] selection = table.getSelectedRows();
				for (int i = 0; i < selection.length; i++) {
					selection[i] = table.convertRowIndexToModel(selection[i]);
					System.out.println(selection[i]);
					rowIndex1 = (Vector) tableModel.getDataVector().get(
							selection[i]);
					try {
						setCustomerDataEdit(rowIndex1);
					} catch (UnknownHostException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		deleteItem.addMouseListener(new java.awt.event.MouseListener() {

			@Override
			public void mouseReleased(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(java.awt.event.MouseEvent e) {
				
				int selection = table.getSelectedRow();
				rowIndex = (Vector) tableModel.getDataVector().get(selection);
				try {
					removeCurrentRow(selection);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		this.table.setModel(tableModel);
	}

	/*
	 * Label setting function.
	 */

	public void labelSetting() {
		label = new JLabel("");
		label.setBounds(0, 0, 156, 92);
		label.setBorder(BorderFactory.createLineBorder(Color.black));
		label.setIcon(new ImageIcon(CustomerList.class
				.getResource("/images/logo.png")));
		panel.add(label);

		label_1 = new JLabel("");
		label_1.setIcon(new ImageIcon(CustomerList.class
				.getResource("/images/list.png")));
		label_1.setBounds(0, 92, 40, 50);
		panel.add(label_1);

		lblCustomerlist = new JLabel("Manage Customer");
		lblCustomerlist.setHorizontalAlignment(SwingConstants.LEFT);
		lblCustomerlist.setBounds(10, 11, 569, 49);
		lblCustomerlist.setOpaque(true);

		lblCustomerlist.setForeground(new Color(255, 255, 255));
		lblCustomerlist.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblCustomerlist.setBackground(new Color(100, 149, 237));
		panel_1.add(lblCustomerlist);
	}

	/*
	 * Button setting function.
	 */
	public void buttonSetting() {
		btnSearch = new JButton("Search");
		btnSearch.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnSearch.setBounds(413, 95, 78, 30);
		panel_1.add(btnSearch);
        search();
        
		btnClear = new JButton("Clear");
		btnClear.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnClear.setBounds(501, 94, 78, 31);
		panel_1.add(btnClear);
		clear();

		btnAdd = new JButton("Add");
		btnAdd.setFont(new Font("Tahoma", Font.BOLD, 11));
		${addButton}
		panel_1.add(btnAdd);

		/*
		 * Using PopupMenu Navigating from customerList page to customerAdd page
		 * for adding new customer to DB .
		 */
		btnAdd.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					dispose();
					customerAdd = new CustomerAdd();
					customerAdd.addRowToDb();
					customerAdd.setVisible(true);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

			}
		});

		btnCustomerlist = new JButton("AddNewCustomer");
		btnCustomerlist.setBounds(28, 103, 128, 28);
		panel.add(btnCustomerlist);
		/*
		 * Using Menu Navigating from customerList page to customerAdd page for
		 * adding new customer to DB .
		 */
		btnCustomerlist.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					dispose();
					customerAdd = new CustomerAdd();
					customerAdd.addRowToDb();
					customerAdd.setVisible(true);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		
		btnEmail = new JButton("Mail");
		btnEmail.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				${emailButtonAction}
			}
		});
		btnEmail.setFont(new Font("Tahoma", Font.BOLD, 11));
		${emailButton}
		panel_1.add(btnEmail);

	}
	

	/*
	 * TextField setting function.
	 */
	public void textFieldSetting() {
		fieldValue = new JTextField();
		fieldValue.setBounds(10, 96, 134, 29);
		panel_1.add(fieldValue);
		fieldValue.setColumns(10);
	}

	/*
	 * ComboBox setting function.
	 */
	public void comboBoxSetting() {
		comboBox = new JComboBox<Object>();
		comboBox.setFont(new Font("Tahoma", Font.BOLD, 11));
		comboBox.setModel(new DefaultComboBoxModel<Object>(new String[] {"Select the field","customerId", "name", "email", "phone",
				"address", "action", "createddate", "createdby",
				"modifieddate", "modifiedby"}));
		comboBox.setToolTipText("");
		comboBox.setBounds(182, 96, 134, 29);
		panel_1.add(comboBox);
	}

	/*
	 * MsgBox setting function.
	 */
	private void msgbox(String s) {
		JOptionPane.showMessageDialog(null, s);
	}

	/*
	 * Set a customer list for delete operation.
	 */
	public void setCustomerDataDelete(Vector vect) throws ParseException {
		
		System.out.println("Entering into Delete of BEAN********");	
		customerList = new ArrayList<Customer>();
		Customer cust = new Customer();
		cust.setCustomerid((Integer) vect.get(0));
		cust.setName((String) vect.get(1));
		cust.setEmail((String) vect.get(2));
		cust.setPhone((String) vect.get(3));
		cust.setAddress((String) vect.get(4));
		cust.setOrders((String) vect.get(5));
		cust.setAction((String) vect.get(6));
		System.out.println("the customerid..." + cust.getCustomerid());
		customerList.add(cust);
		
		HJSFBusinessDelegate delegate = new HJSFBusinessDelegate();
		JSONParser parser = new JSONParser();
		JSONArray array = (JSONArray) parser.parse(customerList.toString());
		log.debug("Total Customer to be deleted : " + array.size());
		String output = delegate.doBusiness("POJO",
				"${Service}", "deleteAll",
				"java.util.List<com.app.customer.vo.Customer>",
				array.toJSONString());
		log.debug("output : " + output);
		log.debug("End of Method Delete..");
	}

	/*
	 * Set a customer object for edit operation.
	 */
	public void setCustomerDataEdit(Vector vect) throws InterruptedException,
			UnknownHostException, ParseException {
	
		HJSFBusinessDelegate delegate = new HJSFBusinessDelegate();
		Customer cust;
		int id = (Integer) vect.get(0);

		JSONObject object = new JSONObject();
		object.put("searchValue", Integer.toString(id));
		object.put("searchColumn", "customerId");

		String output = delegate.doBusiness("POJO",
				"${Service}", "search",
				"String,String", object.toJSONString());

		JSONParser parser = new JSONParser();
		List<Customer> customerList = new ArrayList<>();

		JSONArray customers = (JSONArray) parser.parse(output);
		JSONObject customer = null;
		Customer customerVO = null;
		ObjectMapper mapper = new ObjectMapper();

		for (int i = 0; i < customers.size(); i++) {
			customer = (JSONObject) customers.get(i);
			customerVO = mapper.convertValue(customer, Customer.class);
			customerList.add(customerVO);
		}
		cust = new Customer();
		cust.setCustomerid(customerList.get(0).getCustomerid());
		cust.setName(customerList.get(0).getName());
		cust.setEmail(customerList.get(0).getEmail());
		cust.setPhone(customerList.get(0).getPhone());
		cust.setAddress(customerList.get(0).getAddress());
		cust.setOrders(customerList.get(0).getOrders());
		cust.setAction(customerList.get(0).getAction());
		cust.setCreatedby(customerList.get(0).getCreatedby());
		cust.setCreateddate(customerList.get(0).getCreateddate());
		cust.setModifiedby(InetAddress.getLocalHost().getHostName());
		cust.setModifieddate(new Date());
		editCurrentRow(cust);
	}

	/*
	 * Navigating from customerList page to customerAdd page for editing
	 * existing customer from DB .
	 */
	public void editCurrentRow(Customer cust) throws InterruptedException,
			UnknownHostException {
		System.out.println("Inside editcurrent row");
		dispose();
		customerAdd = new CustomerAdd();
		customerAdd.editData(cust);
		customerAdd.setVisible(true);
	}

	/*
	 * Delete customer from DB.
	 */
	public void removeCurrentRow(int Selection) throws ParseException {
		setCustomerDataDelete(rowIndex);
		if (Selection != -1) {
			((DefaultTableModel) table.getModel()).removeRow(Selection);
		}
		if (tableModel != null) {
			tableModel.fireTableDataChanged();
		}
		msgbox("Record Deleted Successfully");
	}

	/*
	 * Search customer from DB.
	 */
	private void search() {
		btnSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				System.out.println("Entering into Search of BEAN********");
				
				String searchValue = fieldValue.getText();
				String columnValue = (String) comboBox.getSelectedItem();
				// delegate = new CustomerBusinessDelegate();
				JSONObject object = new JSONObject();
				object.put("searchValue", searchValue);
				object.put("searchColumn", columnValue);

				tableModel.setRowCount(0);

				HJSFBusinessDelegate delegate = new HJSFBusinessDelegate();
				String output = delegate.doBusiness("POJO",
						"${Service}",
						"search", "String,String", object.toJSONString());

				JSONParser parser = new JSONParser();
				List<Customer> customerList = new ArrayList<>();
				try {
					JSONArray customers = (JSONArray) parser.parse(output);
					JSONObject customer = null;
					Customer customerVO = null;
					ObjectMapper mapper = new ObjectMapper();

					for (int i = 0; i < customers.size(); i++) {
						customer = (JSONObject) customers.get(i);
						customerVO = mapper.convertValue(customer,
								Customer.class);
						customerList.add(customerVO);
					}

					System.out.println("List size" + customerList.size());
					DisplayData(customerList);
					log.debug("End of Method Search..");
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	
	/*
	 * Clear Field value and refresh table.
	 */
	private void clear() {
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fieldValue.setText("");
				tableModel.setRowCount(0);
				DisplayData(initialiseList());
			}
		});
	}

	private void DisplayData(List<Customer> customerList) {
		// setting the column name
		Object[] tableColumnNames = new Object[7];
		tableColumnNames[0] = "Customer Id";
		tableColumnNames[1] = "Name";
		tableColumnNames[2] = "Email";
		tableColumnNames[3] = "Phone";
		tableColumnNames[4] = "Address";
		tableColumnNames[5] = "Orders";
		tableColumnNames[6] = "Action";

		tableModel.setColumnIdentifiers(tableColumnNames);
		if (customerList == null) {
			this.table.setModel(tableModel);
			return;
		}

		Object[] objects = new Object[7];
		ListIterator<Customer> lstrg = customerList.listIterator();
		// populating the TableModel
		while (lstrg.hasNext()) {
			Customer customer = lstrg.next();
			objects[0] = customer.getCustomerid();
			objects[1] = customer.getName();
			objects[2] = customer.getEmail();
			objects[3] = customer.getPhone();
			objects[4] = customer.getAddress();
			objects[5] = customer.getOrders();
			objects[6] = customer.getAction();
			tableModel.addRow(objects);
		}
		this.table.setModel(tableModel);
		tableModel.fireTableDataChanged();
	}

	public List<Customer> initialiseList() {
		List<Customer> customerList = null;
		try {
			log.debug("Inside Method initialiseList..");
			HJSFBusinessDelegate delegate = new HJSFBusinessDelegate();
			${Cache_ImplStart}
			String output = delegate.doBusiness("POJO",
					"${Service}", "getAllCustomer",
					"", "");
			log.debug("output : " + output);
			JSONParser parser = new JSONParser();
			JSONArray customers = (JSONArray) parser.parse(output);
			log.debug("Customer Array : " + customers);
			customerList = new ArrayList<>();
			for (int i = 0; i < customers.size(); i++) {
				JSONObject customer = (JSONObject) customers.get(i);
				ObjectMapper mapper = new ObjectMapper();
				customerList.add(mapper.convertValue(customer, Customer.class));
			}
			${Cache_ImplEnd}
			log.debug("List Size : " + customerList.size());
			log.debug("End of Method initialiseList..");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return customerList;

	}

}
