package id.nusantararasa.erp.faktur;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record FakturJatuhTempoDto(
        String nomor,
        String kodeCustomer,
        String namaCustomer,
        LocalDate jatuhTempo,
        BigDecimal total,
        long hariLewat) {

    public static FakturJatuhTempoDto dari(Faktur f, LocalDate sampai) {
        return new FakturJatuhTempoDto(f.getNomor(), f.getCustomer().getKode(), f.getCustomer().getNama(),
                f.getJatuhTempo(), f.getTotal(), ChronoUnit.DAYS.between(f.getJatuhTempo(), sampai));
    }
}
