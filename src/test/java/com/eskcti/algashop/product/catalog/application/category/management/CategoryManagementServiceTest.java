package com.eskcti.algashop.product.catalog.application.category.management;

import com.eskcti.algashop.product.catalog.application.ApplicationMessagePublisher;
import com.eskcti.algashop.product.catalog.application.category.event.CategoryUpdatedEvent;
import com.eskcti.algashop.product.catalog.domain.model.category.Category;
import com.eskcti.algashop.product.catalog.domain.model.category.CategoryNotFoundException;
import com.eskcti.algashop.product.catalog.domain.model.category.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CategoryManagementServiceTest {

    private final CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
    private final ApplicationMessagePublisher applicationMessagePublisher = Mockito.mock(ApplicationMessagePublisher.class);
    private final CategoryManagementService service = new CategoryManagementService(categoryRepository, applicationMessagePublisher);

    @Test
    void shouldCreateCategoryAndReturnGeneratedId() {
        CategoryInput input = CategoryInput.builder()
                .name("Notebook")
                .enabled(true)
                .build();

        UUID createdId = service.create(input);

        ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);
        Mockito.verify(categoryRepository).save(captor.capture());
        assertThat(createdId).isEqualTo(captor.getValue().getId());
    }

    @Test
    void shouldUpdateCategory() {
        Category category = new Category("Notebook", true);
        CategoryInput input = CategoryInput.builder()
                .name("Notebook Gamer")
                .enabled(false)
                .build();
        Mockito.when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        service.update(category.getId(), input);

        assertThat(category.getName()).isEqualTo("Notebook Gamer");
        assertThat(category.getEnabled()).isFalse();
        Mockito.verify(categoryRepository).save(category);

        ArgumentCaptor<CategoryUpdatedEvent> eventCaptor = ArgumentCaptor.forClass(CategoryUpdatedEvent.class);
        Mockito.verify(applicationMessagePublisher).send(eventCaptor.capture());
        assertThat(eventCaptor.getValue().getCategoryId()).isEqualTo(category.getId());
        assertThat(eventCaptor.getValue().getName()).isEqualTo("Notebook Gamer");
        assertThat(eventCaptor.getValue().getEnabled()).isFalse();
    }

    @Test
    void shouldDisableCategory() {
        Category category = new Category("Notebook", true);
        Mockito.when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        service.disable(category.getId());

        assertThat(category.getEnabled()).isFalse();
        Mockito.verify(categoryRepository).save(category);

        ArgumentCaptor<CategoryUpdatedEvent> eventCaptor = ArgumentCaptor.forClass(CategoryUpdatedEvent.class);
        Mockito.verify(applicationMessagePublisher).send(eventCaptor.capture());
        assertThat(eventCaptor.getValue().getCategoryId()).isEqualTo(category.getId());
        assertThat(eventCaptor.getValue().getName()).isEqualTo("Notebook");
        assertThat(eventCaptor.getValue().getEnabled()).isFalse();
    }

    @Test
    void shouldThrowWhenUpdatingNonExistentCategory() {
        UUID categoryId = UUID.randomUUID();
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        CategoryInput input = CategoryInput.builder()
                .name("Notebook")
                .enabled(true)
                .build();

        assertThatThrownBy(() -> service.update(categoryId, input))
                .isInstanceOf(CategoryNotFoundException.class);
        Mockito.verify(categoryRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(applicationMessagePublisher, Mockito.never()).send(Mockito.any());
    }

    @Test
    void shouldThrowWhenDisablingNonExistentCategory() {
        UUID categoryId = UUID.randomUUID();
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.disable(categoryId))
                .isInstanceOf(CategoryNotFoundException.class);
        Mockito.verify(categoryRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(applicationMessagePublisher, Mockito.never()).send(Mockito.any());
    }
}
