<?xml version="1.0" encoding="UTF-8"?>

<?import java.lang.*?>
<?import java.net.*?>
<?import javafx.collections.*?>
<?import javafx.scene.*?>
<?import javafx.scene.control.*?>
<?import javafx.scene.control.cell.*?>
<?import javafx.scene.control.cell.PropertyValueFactory?>
<?import javafx.scene.image.*?>
<?import javafx.scene.layout.*?>
<?import javafx.scene.layout.AnchorPane?>
<?import javafx.scene.text.*?>

<AnchorPane cache="false" cacheHint="DEFAULT" disable="false" focusTraversable="false" pickOnBounds="false" prefHeight="706.0" prefWidth="1097.0000999999975" xmlns:fx="http://javafx.com/fxml/1" xmlns="http://javafx.com/javafx/2.2" fx:controller="com.app.customer.controller.CustomerList">
  <!-- TODO Add Nodes -->
  <children>
    <HBox id="HBox" alignment="CENTER" spacing="5.0" AnchorPane.bottomAnchor="1.0" AnchorPane.leftAnchor="0.0" AnchorPane.topAnchor="0.0">
      <children>
        <AnchorPane minHeight="0.0" minWidth="0.0" prefHeight="640.0" prefWidth="196.0" style="-fx-background-color: #00b3b3;" HBox.hgrow="ALWAYS">
          <children>
            <StackPane id="StackPane" layoutX="2.0" layoutY="0.0" style="-fx-background-color:ecf0f1;">
              <children>
                <ImageView cache="true" cacheHint="DEFAULT" depthTest="INHERIT" disable="false" fitHeight="103.0" fitWidth="193.88236011742433" focusTraversable="false" pickOnBounds="true" preserveRatio="true" smooth="true" style="" visible="true">
                  <image>
                    <Image url="@../images/HexawareLogo.png" />
                  </image>
                </ImageView>
              </children>
            </StackPane>
            <StackPane id="StackPane" minHeight="47.0" minWidth="43.999900000002526" prefHeight="47.0" prefWidth="43.999900000002526" AnchorPane.bottomAnchor="492.0" AnchorPane.leftAnchor="2.0" AnchorPane.rightAnchor="150.0" AnchorPane.topAnchor="102.0">
              <children>
                <ImageView fitHeight="44.000115258786536" fitWidth="44.000115258786536" pickOnBounds="true" preserveRatio="true">
                  <image>
                    <Image url="@../images/list.png" />
                  </image>
                </ImageView>
              </children>
            </StackPane>
            <StackPane id="StackPane" AnchorPane.bottomAnchor="499.0" AnchorPane.leftAnchor="39.0" AnchorPane.rightAnchor="0.0" AnchorPane.topAnchor="103.0">
              <children>
                <Hyperlink id="CustomerListlink" prefHeight="39.0" prefWidth="157.0" style="-fx-background-color: #009999;" onAction="#customerListlink" text="AddNewCustomer" textFill="#0021cc" underline="false" visited="true">
                  <font>
                    <Font name="System Bold Italic" size="14.0" />
                  </font>
                </Hyperlink>
              </children>
            </StackPane>
          </children>
        </AnchorPane>
      </children>
    </HBox>
    <HBox id="HBox" alignment="CENTER" spacing="5.0" AnchorPane.bottomAnchor="1.0" AnchorPane.leftAnchor="201.0" AnchorPane.topAnchor="0.0">
      <children>
        <AnchorPane cache="false" minHeight="0.0" minWidth="0.0" prefHeight="705.0" prefWidth="896.0001220703125" HBox.hgrow="ALWAYS">
          <children>
            <Label focusTraversable="false" prefHeight="51.0" prefWidth="791.5" style="-fx-background-color: #00b3b3;&#10;" text="Manage Customer" textFill="WHITE" AnchorPane.leftAnchor="45.0" AnchorPane.rightAnchor="48.5" AnchorPane.topAnchor="31.0">
              <font>
                <Font name="System Bold Italic" size="15.0" />
              </font>
            </Label>
            <Pane prefHeight="423.0" prefWidth="792.0" style="-fx-background-color:#bdc3c7;" AnchorPane.leftAnchor="45.0" AnchorPane.topAnchor="113.0">
              <children>
                <TableView fx:id="customers" blendMode="HARD_LIGHT" focusTraversable="true" layoutX="22.0" layoutY="93.0" minHeight="24.0" pickOnBounds="false" prefHeight="225.0" prefWidth="736.0" style="-fx-background-color: #9CDBD4;" tableMenuButtonVisible="false">
                  <columns>
                    <TableColumn maxWidth="5000.0" minWidth="10.0" prefWidth="89.0" text="CustomerId">
                      <cellValueFactory>
                        <PropertyValueFactory property="customerid" />
                      </cellValueFactory>
                    </TableColumn>
                    <TableColumn maxWidth="5000.0" minWidth="10.0" prefWidth="91.0" text="Name">
                      <cellValueFactory>
                        <PropertyValueFactory property="name" />
                      </cellValueFactory>
                    </TableColumn>
                    <TableColumn maxWidth="5000.0" minWidth="10.0" prefWidth="132.0" text="Email">
                      <cellValueFactory>
                        <PropertyValueFactory property="email" />
                      </cellValueFactory>
                    </TableColumn>
                    <TableColumn maxWidth="5000.0" minWidth="10.0" prefWidth="102.0" text="Phone">
                      <cellValueFactory>
                        <PropertyValueFactory property="phone" />
                      </cellValueFactory>
                    </TableColumn>
                    <TableColumn maxWidth="5000.0" minWidth="10.0" prefWidth="143.0" text="Address">
                      <cellValueFactory>
                        <PropertyValueFactory property="address" />
                      </cellValueFactory>
                    </TableColumn>
                    <TableColumn maxWidth="5000.0" minWidth="10.0" prefWidth="94.0" text="Order">
                      <cellValueFactory>
                        <PropertyValueFactory property="orders" />
                      </cellValueFactory>
                    </TableColumn>
                    <TableColumn maxWidth="5000.0" minWidth="10.0" prefWidth="87.0" text="Action">
                      <cellValueFactory>
                        <PropertyValueFactory property="action" />
                      </cellValueFactory>
                    </TableColumn>
                  </columns>
                  <contextMenu>
                    <ContextMenu>
                      <items>
                        <MenuItem mnemonicParsing="false" onAction="#delete" text="Delete" />
                        <MenuItem mnemonicParsing="false" onAction="#edit" text="Edit" />
                      </items>
                    </ContextMenu>
                  </contextMenu>
                </TableView>
                <Button id="Addnewrow" defaultButton="true" layoutX="491.0" layoutY="348.0" mnemonicParsing="false" onAction="#addNewrow" prefHeight="39.0" prefWidth="96.0001220703125" style="" text="Add">
                 <font>
                    <Font name="System Bold" size="14.0" fx:id="x1" />
                  </font>
                </Button>
				${emailButtonCodeInJavaFx}
                <Button id="search" defaultButton="true" font="$x1" layoutX="491.0" layoutY="29.0" mnemonicParsing="false" onAction="#search" prefHeight="39.0" prefWidth="96.0001220703125" text="Search" />
                <Button fx:id="clear" cancelButton="false" defaultButton="true" font="$x1" layoutX="654.0" layoutY="29.0" minHeight="17.0" mnemonicParsing="false" onAction="#clear" prefHeight="39.0" prefWidth="96.0001220703125" style="" text="Clear" />
                <ComboBox fx:id="comboField" disable="false" editable="false" layoutX="284.0" layoutY="27.0" minHeight="19.0" prefHeight="39.0" prefWidth="149.0" promptText="--Select the field--" style="-fx-background-color: #6DAFE8;&#10;-fx-font-size: 13;&#10;-fx-font-color: #fefefe;" visible="true">
                  <items>
                    <FXCollections fx:factory="observableArrayList">
                      <String fx:value="item1" />
                      <String fx:value="item2" />
                    </FXCollections>
                  </items>
                </ComboBox>
                <TextField fx:id="fieldValue" layoutX="32.0" layoutY="24.0" pickOnBounds="false" prefHeight="44.0" prefWidth="188.0" />
              </children>
            </Pane>
          </children>
        </AnchorPane>
      </children>
    </HBox>
  </children>
</AnchorPane>
