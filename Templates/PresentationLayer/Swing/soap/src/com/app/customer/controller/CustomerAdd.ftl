package com.app.customer.controller;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.UIManager;
${Validation_import}
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

import javax.swing.SwingConstants;

import com.app.customer.vo.Customer;
import com.app.framework.delegate.HJSFBusinessDelegate;
import com.app.framework.exception.HexApplicationException;
import com.hexaware.framework.logger.LogFactory;
import com.hexaware.framework.logger.Logger;

import java.awt.SystemColor;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JScrollPane;

import org.codehaus.jackson.map.ObjectMapper;

public class CustomerAdd extends JFrame {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	public static boolean flag = ${AuthenticationFlagset};
	private JPanel contentPane;
	private JPanel panel;
	private JPanel panel_1;
	private JPanel panel_2;
	private JLabel label;
	private JLabel label_1;
	private JLabel infoLabel;
	private JLabel lblCustomerId;
	private JLabel lblName;
	private JLabel lblPhone;
	private JLabel lblAddress;
	private JLabel lblAction;
	private JLabel lblEmail;
	private JLabel lblOrder;
	private JLabel lblCustomerlist;
	private JLabel lblCreatedDate;
	private JLabel lblCreatedBy;
	private JLabel lblModifiedBy;
	private JLabel lblModifiedDate;
	private JTextField customerId;
	private JTextField name;
	private JTextField email;
	private JTextField phone;
	private JTextField address;
	private JTextField orders;
	private JTextField action;
	private JTextField createdBy;
	private JTextField createdDate;
	private JTextField modifiedBy;
	private JTextField modifiedDate;
	private JButton btnCancel;
	private JButton btnAdd;
	private JButton btnUpdate;
	private JButton btnCustomerlist;
	CustomerList customerList;
	
	static Logger log = LogFactory.getLogger(CustomerAdd.class);
	
	private JScrollPane scrollPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {

				try {
					if(flag)
					{
						CustomerAdd frame = new CustomerAdd();
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
	 * @throws InterruptedException
	 */
	public CustomerAdd() throws InterruptedException {
		start();
	}

	/*
	 * Call all setting function.
	 */
	public void start() {
		panelSetting();
		labelSetting();
		textFieldSetting();
		buttonSetting();
	}

	/*
	 * Panel setting function.
	 */
	public void panelSetting() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 760, 600);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);

		panel = new JPanel();
		panel.setBounds(0, 0, 155, 562);
		contentPane.add(panel);
		panel.setLayout(null);
		panel.setBorder(BorderFactory.createLineBorder(Color.black));

		panel_1 = new JPanel();
		panel_1.setBackground(SystemColor.control);
		panel_1.setForeground(Color.BLACK);
		panel_1.setBounds(155, 0, 589, 562);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		panel_1.setBorder(BorderFactory.createLineBorder(Color.black));

		scrollPane = new JScrollPane();
		scrollPane.setBounds(21, 101, 552, 420);
		panel_1.add(scrollPane);

		panel_2 = new JPanel();
		scrollPane.setViewportView(panel_2);
		panel_2.setLayout(null);
		panel_2.setBorder(BorderFactory.createLineBorder(Color.black));

	}

	/*
	 * Label setting function.
	 */
	public void labelSetting() {

		infoLabel = new JLabel("");
		infoLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		infoLabel.setBounds(118, 57, 341, 33);
		panel_1.add(infoLabel);

		label = new JLabel("");
		label.setBounds(0, 0, 156, 92);
		label.setBorder(BorderFactory.createLineBorder(Color.black));
		label.setIcon(new ImageIcon(CustomerAdd.class.getResource("/images/logo.png")));
		panel.add(label);

		label_1 = new JLabel("");
		label_1.setIcon(new ImageIcon(CustomerAdd.class.getResource("/images/list.png")));
		label_1.setBounds(0, 92, 40, 57);
		panel.add(label_1);

		lblCustomerlist = new JLabel("Adding New Customer");
		lblCustomerlist.setHorizontalAlignment(SwingConstants.LEFT);
		lblCustomerlist.setBounds(21, 11, 552, 48);
		lblCustomerlist.setOpaque(true);
		lblCustomerlist.setForeground(new Color(255, 255, 255));
		lblCustomerlist.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblCustomerlist.setBackground(new Color(100, 149, 237));
		panel_1.add(lblCustomerlist);

		lblCustomerId = new JLabel("Customer Id*");
		lblCustomerId.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblCustomerId.setOpaque(true);
		lblCustomerId.setBackground(new Color(244, 164, 96));
		lblCustomerId.setBounds(31, 21, 150, 25);
		panel_2.add(lblCustomerId);

		lblName = new JLabel("Name");
		lblName.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblName.setOpaque(true);
		lblName.setBackground(new Color(244, 164, 96));
		lblName.setBounds(31, 57, 150, 25);
		panel_2.add(lblName);

		lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblEmail.setOpaque(true);
		lblEmail.setBackground(new Color(244, 164, 96));
		lblEmail.setBounds(31, 93, 150, 27);
		panel_2.add(lblEmail);

		lblPhone = new JLabel("Phone");
		lblPhone.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblPhone.setOpaque(true);
		lblPhone.setBackground(new Color(244, 164, 96));
		lblPhone.setBounds(31, 131, 150, 25);
		panel_2.add(lblPhone);

		lblAddress = new JLabel("Address");
		lblAddress.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblAddress.setOpaque(true);
		lblAddress.setBackground(new Color(244, 164, 96));
		lblAddress.setBounds(31, 167, 150, 25);
		panel_2.add(lblAddress);

		lblOrder = new JLabel("Order");
		lblOrder.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblOrder.setOpaque(true);
		lblOrder.setBackground(new Color(244, 164, 96));
		lblOrder.setBounds(31, 203, 150, 27);
		panel_2.add(lblOrder);

		lblAction = new JLabel("Action");
		lblAction.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblAction.setOpaque(true);
		lblAction.setBackground(new Color(244, 164, 96));
		lblAction.setBounds(31, 241, 150, 25);
		panel_2.add(lblAction);

		lblCreatedBy = new JLabel("Created By");
		lblCreatedBy.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblCreatedBy.setBackground(new Color(244, 164, 96));
		lblCreatedBy.setOpaque(true);
		lblCreatedBy.setBounds(31, 277, 150, 23);
		panel_2.add(lblCreatedBy);

		lblCreatedDate = new JLabel("Created Date");
		lblCreatedDate.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblCreatedDate.setBackground(new Color(244, 164, 96));
		lblCreatedDate.setOpaque(true);
		lblCreatedDate.setBounds(31, 311, 150, 25);
		panel_2.add(lblCreatedDate);

		lblModifiedBy = new JLabel("Modified By");
		lblModifiedBy.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblModifiedBy.setBackground(new Color(244, 164, 96));
		lblModifiedBy.setOpaque(true);
		lblModifiedBy.setBounds(31, 347, 150, 25);
		panel_2.add(lblModifiedBy);

		lblModifiedDate = new JLabel("Modified Date");
		lblModifiedDate.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblModifiedDate.setBackground(new Color(244, 164, 96));
		lblModifiedDate.setOpaque(true);
		lblModifiedDate.setBounds(31, 383, 150, 22);
		panel_2.add(lblModifiedDate);

	}

	/*
	 * TextField Setting function.
	 */
	public void textFieldSetting() {
		customerId = new JTextField();
		customerId.setBounds(219, 23, 193, 23);
		panel_2.add(customerId);
		customerId.setColumns(10);

		name = new JTextField();
		name.setBounds(219, 59, 193, 23);
		panel_2.add(name);
		name.setColumns(10);

		email = new JTextField();
		email.setBounds(219, 95, 193, 24);
		panel_2.add(email);
		email.setColumns(10);

		phone = new JTextField();
		phone.setBounds(219, 133, 193, 23);
		panel_2.add(phone);
		phone.setColumns(10);

		address = new JTextField();
		address.setBounds(219, 168, 193, 25);
		panel_2.add(address);
		address.setColumns(10);

		orders = new JTextField();
		orders.setBounds(219, 204, 193, 27);
		panel_2.add(orders);
		orders.setColumns(10);

		action = new JTextField();
		action.setBounds(219, 242, 193, 25);
		panel_2.add(action);
		action.setColumns(10);

		createdBy = new JTextField();
		createdBy.setEditable(false);
		createdBy.setBounds(219, 278, 193, 22);
		panel_2.add(createdBy);
		createdBy.setColumns(10);

		createdDate = new JTextField();
		createdDate.setEditable(false);
		createdDate.setBounds(219, 312, 193, 25);
		panel_2.add(createdDate);
		createdDate.setColumns(10);

		modifiedBy = new JTextField();
		modifiedBy.setEditable(false);
		modifiedBy.setBounds(219, 348, 193, 25);
		panel_2.add(modifiedBy);
		modifiedBy.setColumns(10);

		modifiedDate = new JTextField();
		modifiedDate.setEditable(false);
		modifiedDate.setBounds(219, 383, 193, 25);
		panel_2.add(modifiedDate);
		modifiedDate.setColumns(10);
	}

	public void buttonSetting() {

		btnCustomerlist = new JButton("CustomerList");
		btnCustomerlist.setBounds(29, 103, 127, 28);
		panel.add(btnCustomerlist);
		/*
		 * Using Menu Navigating from customerList page to customerAdd page for
		 * listing customer details.
		 */
		btnCustomerlist.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
					try {
						customerList = new CustomerList();
						customerList.start();
					} catch (HexApplicationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
					customerList.setVisible(true);
			}
		});

		btnAdd = new JButton("Add");
		btnAdd.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnAdd.setBounds(453, 126, 78, 36);
		panel_2.add(btnAdd);

		btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnCancel.setBounds(453, 184, 78, 36);
		panel_2.add(btnCancel);

		/*
		 * Navigating from customerList page to customerAdd page.
		 */
		btnCancel.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
				try {
					customerList = new CustomerList();
					customerList.start();
					customerList.setVisible(true);
				} catch (HexApplicationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		btnUpdate = new JButton("Update");
		btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnUpdate.setBounds(453, 241, 78, 33);
		panel_2.add(btnUpdate);

		/*
		 * After update navigate from customerList page to customerAdd page.
		 */
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateRowToDb();
			}
		});
	}

	/*
	 * Navigating to CustomerList page
	 */
	public void customerListlink() throws HexApplicationException {
		dispose();
		customerList = new CustomerList();
		customerList.setVisible(true);
	}

	/*
	 * Adding customer into DataBase
	 */
	public void addRowToDb() {
		btnUpdate.setVisible(false);
		createdBy.setVisible(false);
		createdDate.setVisible(false);
		modifiedBy.setVisible(false);
		modifiedDate.setVisible(false);
		lblCreatedBy.setVisible(false);
		lblCreatedDate.setVisible(false);
		lblModifiedBy.setVisible(false);
		lblModifiedDate.setVisible(false);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("getting into add method");
				try {
					/*
					 * Logic
					 */
					Customer customer = new Customer();
					customer.setName(name.getText());
					customer.setEmail(email.getText());
					customer.setAction(action.getText());
					customer.setAddress(address.getText());
					customer.setOrders(orders.getText());
					String id = customerId.getText();
					int customerid = Integer.parseInt(id);
					customer.setCustomerid(customerid);
					customer.setPhone(phone.getText());
					customer.setCreateddate(new Date());
					customer.setModifieddate(new Date());
					customer.setCreatedby(InetAddress.getLocalHost()
							.getHostName());
					customer.setModifiedby(InetAddress.getLocalHost()
							.getHostName());

					/*
					 * save the customer into DB
					 */
					if (insert(customer)) {
						msgbox("Record Inserted Successfully");

						/*
						 * Navigating to List page
						 */
						customerListlink();
					}
				} catch (HexApplicationException exp) {
					System.err.print(exp.getMessage());
					infoLabel.setText(exp.getMessage());
				} catch (Exception exp1) {
					System.err.print(exp1.getMessage());
					infoLabel.setText(exp1.getMessage());
				}
				System.out.println("Exiting add method");
			}
		});
	}

	/*
	 * Updating customer into DataBase
	 */
	private void updateRowToDb() {
		System.out.println("getting into update method");
		try {
			/*
			 * Logic
			 */
			Customer customer = new Customer();
			customer.setName(name.getText());
			customer.setEmail(email.getText());
			customer.setAction(action.getText());
			customer.setAddress(address.getText());
			customer.setOrders(orders.getText());
			String id = customerId.getText();
			int customerId = Integer.parseInt(id);
			customer.setCustomerid(customerId);
			customer.setPhone(phone.getText());
			customer.setCreateddate(new Date());
			customer.setModifieddate(new Date());
			customer.setCreatedby(createdBy.getText());
			customer.setModifiedby(modifiedBy.getText());
			
			/*
			 * update the customer into DB
			 */

			if (update(customer)) {
				msgbox("Record Updated Successfully");
				/*
				 * Navigating to List page
				 */
				customerListlink();
			}
		} catch (HexApplicationException e) {
			System.err.print(e.getMessage());
			infoLabel.setText(e.getMessage());
		} catch (Exception e) {
			System.err.print(e.getMessage());
			infoLabel.setText(e.getMessage());
		}
		System.out.println("Exiting update method");

	}

	/*
	 * Retrieving customer details from DB for edit operation
	 */

	public void editData(Customer customer) throws UnknownHostException {
		System.out.println("Inside Edit Data" + customer.getName());
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		customerId.setEditable(false);
		createdDate.setEditable(false);
		createdBy.setEditable(false);
		modifiedBy.setEditable(false);
		customerId.setText(Integer.toString(customer.getCustomerid()));
		name.setText(customer.getName());
		email.setText(customer.getEmail());
		action.setText(customer.getAction());
		address.setText(customer.getAddress());
		phone.setText(customer.getPhone());
		orders.setText(customer.getOrders());
		createdDate.setText(sdf.format(customer.getCreateddate()));
		modifiedDate.setText(sdf.format(customer.getModifieddate()));
		createdBy.setText(customer.getCreatedby());
		modifiedBy.setText(customer.getModifiedby());
		btnUpdate.setVisible(true);
		btnAdd.setVisible(false);
		btnCancel.setVisible(true);
	}

	/*
	 * MessageBox for displaying message.
	 */
	private void msgbox(String s) {
		JOptionPane.showMessageDialog(null, s);
	}
	
	public boolean insert(Customer customer) throws IOException {

		log.debug("Inside Method Insert..");
		HJSFBusinessDelegate delegate = new HJSFBusinessDelegate();
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(customer);
		try
		{
		${Validation_Call}
		String output = delegate.doBusiness("POJO",
				"com.app.customer.stub.CustomerStub", "insert",
				"com.app.customer.vo.Customer", jsonInString);
		log.debug("output : " + output);
		}${Validation_CatchAdd}
		catch (Exception e) {
			System.err.print(e.getMessage());
			infoLabel.setText(e.getMessage());
		}
		log.debug("End of Method Insert..");
		return true;
	}

	public boolean update(Customer customer) throws IOException {

		log.debug("Inside Method Update..");
		HJSFBusinessDelegate delegate = new HJSFBusinessDelegate();
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(customer);
		try
		{
		${Validation_Call}
		String output = delegate.doBusiness("POJO",
				"com.app.customer.stub.CustomerStub", "update",
				"com.app.customer.vo.Customer", jsonInString);
		log.debug("output : " + output);
		}${Validation_CatchAdd}
		catch (Exception e) {
			System.err.print(e.getMessage());
			infoLabel.setText(e.getMessage());
		}
		log.debug("End of Method Update..");
		return true;
	}
}
