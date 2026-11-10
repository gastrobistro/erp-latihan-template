package id.nusantararasa.erp.faktur;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class FakturJatuhTempoApiTest {

    private static final String URL = "/api/faktur/jatuh-tempo";

    @Autowired
    MockMvc mvc;

    @Test
    void belumLunasJatuhTempoSampaiTanggalUrutJatuhTempo() throws Exception {
        mvc.perform(get(URL).param("sampai", "2026-11-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nomor").value("INV/2026/10/00006"))
                .andExpect(jsonPath("$[0].kodeCustomer").value("CUS-021"))
                .andExpect(jsonPath("$[0].jatuhTempo").value("2026-10-22"))
                .andExpect(jsonPath("$[1].nomor").value("INV/2026/10/00005"))
                .andExpect(jsonPath("$[1].kodeCustomer").value("CUS-013"))
                .andExpect(jsonPath("$[1].jatuhTempo").value("2026-11-06"));
    }

    @Test
    void itemBerisiFieldDanHariLewat() throws Exception {
        mvc.perform(get(URL).param("sampai", "2026-11-10"))
                .andExpect(jsonPath("$[0].namaCustomer").isNotEmpty())
                .andExpect(jsonPath("$[0].total").value(36750000.0))
                .andExpect(jsonPath("$[0].hariLewat").value(19))
                .andExpect(jsonPath("$[1].total").value(55554000.0))
                .andExpect(jsonPath("$[1].hariLewat").value(4));
    }

    @Test
    void tanggalSampaiIkutDihitungDanLunasTidakIkut() throws Exception {
        mvc.perform(get(URL).param("sampai", "2026-11-16"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].nomor").value("INV/2026/10/00006"))
                .andExpect(jsonPath("$[1].nomor").value("INV/2026/10/00005"))
                .andExpect(jsonPath("$[2].nomor").value("INV/2026/10/00008"))
                .andExpect(jsonPath("$[3].nomor").value("INV/2026/10/00002"))
                .andExpect(jsonPath("$[3].jatuhTempo").value("2026-11-16"))
                .andExpect(jsonPath("$[3].hariLewat").value(0));
    }

    @Test
    void belumAdaYangJatuhTempoMengembalikanDaftarKosong() throws Exception {
        mvc.perform(get(URL).param("sampai", "2026-10-21"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void sampaiWajibDiisi() throws Exception {
        mvc.perform(get(URL)).andExpect(status().isBadRequest());
    }

    @Test
    void sampaiKosongDitolak() throws Exception {
        mvc.perform(get(URL).param("sampai", "")).andExpect(status().isBadRequest());
    }

    @Test
    void formatSelainIsoDitolak() throws Exception {
        mvc.perform(get(URL).param("sampai", "10-11-2026")).andExpect(status().isBadRequest());
    }

    @Test
    void tanggalYangTidakAdaDitolak() throws Exception {
        mvc.perform(get(URL).param("sampai", "2026-02-30")).andExpect(status().isBadRequest());
    }
}
