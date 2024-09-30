// Implementador (Sistema de Pago)
interface SistemaPago {
    void realizarPago(double monto);
}

class PagoTarjetaCredito implements SistemaPago {
    @Override
    public void realizarPago(double monto) {
        System.out.println("Pago de " + monto + " realizado con tarjeta de crédito.");
    }
}

class PagoPayPal implements SistemaPago {
    @Override
    public void realizarPago(double monto) {
        System.out.println("Pago de " + monto + " realizado a través de PayPal.");
    }
}

// Abstracción (Pedido)
abstract class Pedido {
    protected SistemaPago sistemaPago;

    public Pedido(SistemaPago sistemaPago) {
        this.sistemaPago = sistemaPago;
    }

    abstract String obtenerDescripcion();

    public void procesarPago(double monto) {
        sistemaPago.realizarPago(monto);
    }
}

class PedidoLibro extends Pedido {
    private String titulo;
    private String autor;
    private double precio;

    public PedidoLibro(SistemaPago sistemaPago, String titulo, String autor, double precio) {
        super(sistemaPago);
        this.titulo = titulo;
        this.autor = autor;
        this.precio = precio;
    }

    @Override
    String obtenerDescripcion() {
        return "Libro: " + titulo + " de " + autor + " - Precio: " + precio;
    }
}

class PedidoRopa extends Pedido {
    private String prenda;
    private String talla;
    private double precio;

    public PedidoRopa(SistemaPago sistemaPago, String prenda, String talla, double precio) {
        super(sistemaPago);
        this.prenda = prenda;
        this.talla = talla;
        this.precio = precio;
    }

    @Override
    String obtenerDescripcion() {
        return "Prenda: " + prenda + " - Talla: " + talla + " - Precio: " + precio;
    }
}

// Uso
public class Main {
    public static void main(String[] args) {
        Pedido pedidoLibro = new PedidoLibro(new PagoTarjetaCredito(), "Cien años de soledad", "Gabriel García Márquez", 25.99);
        System.out.println(pedidoLibro.obtenerDescripcion());
        pedidoLibro.procesarPago(25.99);

        Pedido pedidoRopa = new PedidoRopa(new PagoPayPal(), "Camisa", "L", 39.99);
        System.out.println(pedidoRopa.obtenerDescripcion());
        pedidoRopa.procesarPago(39.99);
    }
}