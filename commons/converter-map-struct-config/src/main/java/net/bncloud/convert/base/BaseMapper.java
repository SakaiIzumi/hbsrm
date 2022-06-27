package net.bncloud.convert.base;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Stream;

@MapperConfig(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BaseMapper<D, E> {
    /**
     * DTO转Entity
     * @param dto /
     * @return /
     */
    E toEntity(D dto);

    /**
     * Entity转DTO
     * @param entity /
     * @return /
     */
    D toDto(E entity);

    /**
     * DTO集合转Entity集合
     * @param dtoList /
     * @return /
     */
    List <E> toEntity(List<D> dtoList);

    /**
     * Entity集合转DTO集合
     * @param entityList /
     * @return /
     */
    List <D> toDto(List<E> entityList);

    /**
     * DTO stream 转 Entity 集合
     */
    List<E> toEntity(Stream<D> stream);

    /**
     * Entity stream 转 DTO 集合
     */
    List<D> toDto(Stream<E> stream);
}
