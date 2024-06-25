package skincare;

import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FormBarang extends JFrame {
    private String[] judul = {"Kode Barang", "Nama Barang", "Harga", "Stok"};
    DefaultTableModel df;
    JTable tab = new JTable();
    JScrollPane scp = new JScrollPane();
    JPanel pnl = new JPanel();
    JLabel lblNama = new JLabel("Nama Barang");
    JTextField txNama = new JTextField(20);
    JLabel lblKode = new JLabel("Kode Barang");
    JTextField txKode = new JTextField(10);
    JLabel lblHarga = new JLabel("Harga");
    JTextField txHarga = new JTextField(10);
    JLabel lblStok = new JLabel("Stok");
    JTextField txStok = new JTextField(5);
    JButton btTambah = new JButton("Simpan");
    JButton btBaru = new JButton("Baru");
    JButton btHapus = new JButton("Hapus");
    JButton btUbah = new JButton("Ubah");

    public FormBarang() {
        super("Data Barang");
        setSize(600, 350);
        pnl.setLayout(null);
        
        pnl.add(lblKode);
        lblKode.setBounds(20, 10, 80, 20);
        pnl.add(txKode);
        txKode.setBounds(140, 10, 100, 20);
        
        pnl.add(lblNama);
        lblNama.setBounds(20, 35, 100, 20);
        pnl.add(txNama);
        txNama.setBounds(140, 35, 250, 20);
        
        pnl.add(lblHarga);
        lblHarga.setBounds(20, 60, 80, 20);
        pnl.add(txHarga);
        txHarga.setBounds(140, 60, 100, 20);
        
        pnl.add(lblStok);
        lblStok.setBounds(20, 85, 80, 20);
        pnl.add(txStok);
        txStok.setBounds(140, 85, 50, 20);
        
        pnl.add(btBaru);
        btBaru.setBounds(20, 120, 125, 20);
        btBaru.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btBaruAksi(e);
            }
        });
        
        pnl.add(btTambah);
        btTambah.setBounds(160, 120, 125, 20);
        btTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btTambahAksi(e);
            }
        });
        
        pnl.add(btUbah);
        btUbah.setBounds(300, 120, 125, 20);
        btUbah.setEnabled(false);
        btUbah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btUbahAksi(e);
            }
        });
        
        pnl.add(btHapus);
        btHapus.setBounds(440, 120, 125, 20);
        btHapus.setEnabled(false);
        btHapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btHapusAksi(e);
            }
        });

        df = new DefaultTableModel(null, judul);
        tab.setModel(df);
        scp.getViewport().add(tab);
        tab.setEnabled(true);
        tab.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                tabMouseClicked(evt);
            }
        });
        scp.setBounds(20, 160, 540, 130);
        pnl.add(scp);
        
        getContentPane().add(pnl);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    void loadData() {
        try {
            Connection cn = new ConnecDB().getConnect(); // Menggunakan koneksi dari ConnecDB
            String sql = "SELECT * FROM tbl_barang"; // Query untuk memilih semua data dari tabel tbl_barang
            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            // Hapus semua baris yang ada di tabel df sebelum memuat data baru
            clearTable();
            
            // Memuat data dari hasil query ke dalam tabel df (DefaultTableModel)
            while (rs.next()) {
                String kode, nama, harga, stok;
                kode = rs.getString("kode_barang");
                nama = rs.getString("nama_barang");
                harga = rs.getString("harga");
                stok = rs.getString("stok");
                String[] data = {kode, nama, harga, stok};
                df.addRow(data);
            }
            
            rs.close();
            ps.close();
            cn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void clearTable() {
        int numRow = df.getRowCount();
        for (int i = 0; i < numRow; i++) {
            df.removeRow(0);
        }
    }

    void clearTextField() {
        txKode.setText(null);
        txNama.setText(null);
        txHarga.setText(null);
        txStok.setText(null);
    }

    void simpanData(Barang B) {
        try {
            Connection cn = new ConnecDB().getConnect(); // Ganti ConnecDB dengan kelas koneksi Anda
            Statement st = cn.createStatement();
            String sql = "INSERT INTO tbl_barang (kode_barang, nama_barang, harga, stok) " +
                    "VALUES ('" + B.getKodeBarang() + "', '" + B.getNamaBarang() + "', '" + B.getHarga() + "', '" + B.getStok() + "')";
            int result = st.executeUpdate(sql);
            cn.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan",
                    "Info Proses", JOptionPane.INFORMATION_MESSAGE);
            String[] data = {B.getKodeBarang(), B.getNamaBarang(), B.getHarga(), B.getStok()};
            df.addRow(data);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void hapusData(String kodeBarang) {
        try {
            Connection cn = new ConnecDB().getConnect(); // Ganti ConnecDB dengan kelas koneksi Anda
            Statement st = cn.createStatement();
            String sql = "DELETE FROM tbl_barang WHERE kode_barang = '" + kodeBarang + "'";
            int result = st.executeUpdate(sql);
            cn.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus", "Info Proses",
                    JOptionPane.INFORMATION_MESSAGE);
            df.removeRow(tab.getSelectedRow());
            clearTextField();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void ubahData(Barang B, String kodeBarang) {
        try {
            Connection cn = new ConnecDB().getConnect(); // Ganti ConnecDB dengan kelas koneksi Anda
            Statement st = cn.createStatement();
            String sql = "UPDATE tbl_barang SET kode_barang='" + B.getKodeBarang() + "', nama_barang='" + B.getNamaBarang() + "', " +
                    "harga='" + B.getHarga() + "', stok='" + B.getStok() + "' WHERE kode_barang='" + kodeBarang + "'";
            int result = st.executeUpdate(sql);
            cn.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah", "Info Proses",
                    JOptionPane.INFORMATION_MESSAGE);
            clearTable();
            loadData();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void btBaruAksi(ActionEvent evt) {
        clearTextField();
        btUbah.setEnabled(false);
        btHapus.setEnabled(false);
        btTambah.setEnabled(true);
    }

    private void btTambahAksi(ActionEvent evt) {
        Barang B = new Barang();
        B.setKodeBarang(txKode.getText());
        B.setNamaBarang(txNama.getText());
        B.setHarga(txHarga.getText());
        B.setStok(txStok.getText());
        simpanData(B);
    }

    private void btHapusAksi(ActionEvent evt) {
        int status;
        status = JOptionPane.showConfirmDialog(null, "Yakin data akan dihapus?",
                "Konfirmasi", JOptionPane.OK_CANCEL_OPTION);
        if (status == 0) {
            hapusData(txKode.getText());
        }
    }

    private void btUbahAksi(ActionEvent evt) {
        Barang B = new Barang();
        B.setKodeBarang(txKode.getText());
        B.setNamaBarang(txNama.getText());
        B.setHarga(txHarga.getText());
        B.setStok(txStok.getText());
        ubahData(B, tab.getValueAt(tab.getSelectedRow(), 0).toString());
    }

    private void tabMouseClicked(MouseEvent evt) {
        btUbah.setEnabled(true);
        btHapus.setEnabled(true);
        btTambah.setEnabled(false);
        String kode, nama, harga, stok;
        kode = tab.getValueAt(tab.getSelectedRow(), 0).toString();
        nama = tab.getValueAt(tab.getSelectedRow(), 1).toString();
        harga = tab.getValueAt(tab.getSelectedRow(), 2).toString();
        stok = tab.getValueAt(tab.getSelectedRow(), 3).toString();
        txKode.setText(kode);
        txNama.setText(nama);
        txHarga.setText(harga);
        txStok.setText(stok);
    }

    public static void main(String[] args) {
        new FormBarang().loadData();
    }
}