package polytech.g6.blog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import polytech.g6.blog.web.rest.TestUtil;

class MesureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mesure.class);
        Mesure mesure1 = new Mesure();
        mesure1.setId(1L);
        Mesure mesure2 = new Mesure();
        mesure2.setId(mesure1.getId());
        assertThat(mesure1).isEqualTo(mesure2);
        mesure2.setId(2L);
        assertThat(mesure1).isNotEqualTo(mesure2);
        mesure1.setId(null);
        assertThat(mesure1).isNotEqualTo(mesure2);
    }
}
