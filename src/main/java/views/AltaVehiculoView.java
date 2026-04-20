/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views;

import domain.*;
import data.Persistencia;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AltaVehiculoView extends javax.swing.JDialog {

    private ListarVehiculosView parent;

    private javax.swing.JLabel lblTipo;
    private javax.swing.JComboBox<String> cbTipo;
    private javax.swing.JLabel lblPatente;
    private javax.swing.JTextField txtPatente;
    private javax.swing.JLabel lblMarca;
    private javax.swing.JTextField txtMarca;
    private javax.swing.JLabel lblModelo;
    private javax.swing.JTextField txtModelo;
    private javax.swing.JLabel lblAnio;
    private javax.swing.JTextField txtAnio;
    private javax.swing.JLabel lblCapacidad;
    private javax.swing.JTextField txtCapacidad;
    private javax.swing.JLabel lblSucursal;
    private javax.swing.JComboBox<String> cbSucursal;

    // Campos específicos combustible
    private javax.swing.JLabel lblKmPorLitro;
    private javax.swing.JTextField txtKmPorLitro;
    private javax.swing.JLabel lblLitrosExtra;
    private javax.swing.JTextField txtLitrosExtra;

    // Campos específicos eléctrico
    private javax.swing.JLabel lblKwhBase;
    private javax.swing.JTextField txtKwhBase;

    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JPanel panelEspecifico;

    public AltaVehiculoView(ListarVehiculosView parent) {
        super(parent, "Alta de Vehículo", true);
        this.parent = parent;
        initComponents();
        cargarSucursales();
        actualizarCamposEspecificos();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(420, 480));
        setResizable(false);

        lblTipo = new JLabel("Tipo de Vehículo:");
        cbTipo = new JComboBox<>(new String[]{"COMBUSTIBLE", "ELECTRICO"});
        cbTipo.addActionListener(e -> actualizarCamposEspecificos());

        lblPatente = new JLabel("Patente:");
        txtPatente = new JTextField();
        lblMarca = new JLabel("Marca:");
        txtMarca = new JTextField();
        lblModelo = new JLabel("Modelo:");
        txtModelo = new JTextField();
        lblAnio = new JLabel("Año:");
        txtAnio = new JTextField();
        lblCapacidad = new JLabel("Capacidad de Carga (kg):");
        txtCapacidad = new JTextField();
        lblSucursal = new JLabel("Sucursal:");
        cbSucursal = new JComboBox<>();

        panelEspecifico = new JPanel(new GridLayout(0, 2, 8, 8));
        panelEspecifico.setBorder(BorderFactory.createTitledBorder("Datos específicos"));

        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());

        // Layout principal
        JPanel panelForm = new JPanel(new GridLayout(0, 2, 8, 8));
        panelForm.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        panelForm.add(lblTipo);       panelForm.add(cbTipo);
        panelForm.add(lblPatente);    panelForm.add(txtPatente);
        panelForm.add(lblMarca);      panelForm.add(txtMarca);
        panelForm.add(lblModelo);     panelForm.add(txtModelo);
        panelForm.add(lblAnio);       panelForm.add(txtAnio);
        panelForm.add(lblCapacidad);  panelForm.add(txtCapacidad);
        panelForm.add(lblSucursal);   panelForm.add(cbSucursal);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        JPanel panelEspecificoWrapper = new JPanel(new BorderLayout());
        panelEspecificoWrapper.setBorder(BorderFactory.createEmptyBorder(0, 15, 5, 15));
        panelEspecificoWrapper.add(panelEspecifico, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(panelForm, BorderLayout.NORTH);
        add(panelEspecificoWrapper, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
    }

    private void cargarSucursales() {
        cbSucursal.removeAllItems();
        for (Sucursal s : Controlador.getSucursales()) {
            cbSucursal.addItem(s.getCodigo() + " - " + s.getCiudad());
        }
    }

    private void actualizarCamposEspecificos() {
        panelEspecifico.removeAll();
        String tipo = (String) cbTipo.getSelectedItem();

        if ("COMBUSTIBLE".equals(tipo)) {
            lblKmPorLitro = new JLabel("Km por litro:");
            txtKmPorLitro = new JTextField();
            lblLitrosExtra = new JLabel("Litros extra:");
            txtLitrosExtra = new JTextField();
            panelEspecifico.add(lblKmPorLitro);
            panelEspecifico.add(txtKmPorLitro);
            panelEspecifico.add(lblLitrosExtra);
            panelEspecifico.add(txtLitrosExtra);
        } else {
            lblKwhBase = new JLabel("kWh base (por 100km):");
            txtKwhBase = new JTextField();
            panelEspecifico.add(lblKwhBase);
            panelEspecifico.add(txtKwhBase);
        }

        panelEspecifico.revalidate();
        panelEspecifico.repaint();
    }

    private void guardar() {
        try {
            String patente = txtPatente.getText().trim();
            String marca = txtMarca.getText().trim();
            String modelo = txtModelo.getText().trim();
            int anio = Integer.parseInt(txtAnio.getText().trim());
            double capacidad = Double.parseDouble(txtCapacidad.getText().trim());
            int idxSucursal = cbSucursal.getSelectedIndex();

            if (patente.isEmpty() || marca.isEmpty() || modelo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Patente, marca y modelo son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar patente duplicada
            if (Persistencia.getVehiculo(patente).isPresent()) {
                JOptionPane.showMessageDialog(this, "Ya existe un vehículo con esa patente.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Sucursal sucursal = Controlador.getSucursales().get(idxSucursal);
            String tipo = (String) cbTipo.getSelectedItem();
            Vehiculo nuevo;

            if ("COMBUSTIBLE".equals(tipo)) {
                double kmPorLitro = Double.parseDouble(txtKmPorLitro.getText().trim());
                double litrosExtra = Double.parseDouble(txtLitrosExtra.getText().trim());
                nuevo = new VehiculoCombustible(patente, marca, modelo, anio, capacidad, sucursal, kmPorLitro, litrosExtra);
            } else {
                double kwhBase = Double.parseDouble(txtKwhBase.getText().trim());
                nuevo = new VehiculoElectrico(patente, marca, modelo, anio, capacidad, sucursal, kwhBase);
            }

            Controlador.agregarVehiculo(nuevo);
            parent.refrescarVehiculos();
            JOptionPane.showMessageDialog(this, "Vehículo agregado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor ingresá valores numéricos válidos.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }
}
/**
 *
 * @author User
 */


