package com.fersal.fersal.catalogo.producto.service;

import com.fersal.fersal.catalogo.categoria.repository.CategoriaRepository;
import com.fersal.fersal.catalogo.producto.dto.ProductoRequest;
import com.fersal.fersal.catalogo.producto.dto.ProductoResponse;
import com.fersal.fersal.catalogo.producto.entity.Producto;
import com.fersal.fersal.catalogo.producto.mapper.ProductoMapper;
import com.fersal.fersal.catalogo.producto.repository.ProductoRepository;
import com.fersal.fersal.catalogo.proveedor.repository.ProveedorRepository;
import com.fersal.fersal.empresa.repository.EmpresaRepository;
import com.fersal.fersal.storage.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl
        implements ProductoService {

    private final ProductoRepository repository;

    private final EmpresaRepository empresaRepository;

    private final CategoriaRepository categoriaRepository;

    private final ProveedorRepository proveedorRepository;

    private final ProductoMapper mapper;

    private final FileStorageService fileStorageService;

    @Override
    public ProductoResponse crear(
            ProductoRequest request
    ) {

        if (repository.existsByEmpresaIdAndCodigo(
                request.getEmpresaId(),
                request.getCodigo()
        )) {

            throw new IllegalArgumentException(
                    "Ya existe un producto con ese código en la empresa"
            );
        }

        Producto producto =
                Producto.builder()

                        .empresa(
                                empresaRepository.findById(
                                        request.getEmpresaId()
                                ).orElseThrow(
                                        () -> new IllegalArgumentException(
                                                "Empresa no encontrada"
                                        )
                                )
                        )

                        .categoria(
                                request.getCategoriaId() != null
                                        ? categoriaRepository.findById(
                                        request.getCategoriaId()
                                ).orElse(null)
                                        : null
                        )

                        .proveedor(
                                request.getProveedorId() != null
                                        ? proveedorRepository.findById(
                                        request.getProveedorId()
                                ).orElse(null)
                                        : null
                        )

                        .codigo(request.getCodigo())

                        .nombre(request.getNombre())

                        .descripcion(request.getDescripcion())

                        .precioCompra(
                                request.getPrecioCompra()
                        )

                        .precioVenta(
                                request.getPrecioVenta()
                        )

                        .stockMinimo(
                                request.getStockMinimo()
                        )

                        .activo(true)

                        .build();

        return mapper.toResponse(
                repository.save(producto)
        );
    }

    @Override
    public List<ProductoResponse> listar() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductoResponse> listarEmpresa(
            UUID empresaId
    ) {

        return repository
                .findByEmpresaId(empresaId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public ProductoResponse buscar(
            UUID id
    ) {

        return mapper.toResponse(
                repository.findById(id)
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Producto no encontrado"
                                )
                        )
        );
    }

    @Override
    public ProductoResponse actualizar(
            UUID id,
            ProductoRequest request
    ) {

        Producto p =
                repository.findById(id)
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Producto no encontrado"
                                )
                        );

        p.setNombre(
                request.getNombre()
        );

        p.setDescripcion(
                request.getDescripcion()
        );

        p.setPrecioCompra(
                request.getPrecioCompra()
        );

        p.setPrecioVenta(
                request.getPrecioVenta()
        );

        p.setStockMinimo(
                request.getStockMinimo()
        );

        if (request.getCategoriaId() != null) {

            p.setCategoria(
                    categoriaRepository.findById(
                            request.getCategoriaId()
                    ).orElse(null)
            );

        } else {

            p.setCategoria(null);
        }

        if (request.getProveedorId() != null) {

            p.setProveedor(
                    proveedorRepository.findById(
                            request.getProveedorId()
                    ).orElse(null)
            );

        } else {

            p.setProveedor(null);
        }

        return mapper.toResponse(
                repository.save(p)
        );
    }

    @Override
    public ProductoResponse subirImagen(
            UUID productoId,
            MultipartFile imagen
    ) {

        Producto producto =
                repository.findById(productoId)
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Producto no encontrado"
                                )
                        );

        String imagenUrl =
                fileStorageService.guardarImagenProducto(
                        producto.getEmpresa().getId(),
                        producto.getId(),
                        imagen
                );

        producto.setImagenUrl(
                imagenUrl
        );

        return mapper.toResponse(
                repository.save(producto)
        );
    }

    @Override
    public void eliminarImagen(
            UUID productoId
    ) {

        Producto producto =
                repository.findById(productoId)
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Producto no encontrado"
                                )
                        );

        fileStorageService.eliminarImagenProducto(
                producto.getEmpresa().getId(),
                producto.getId()
        );

        producto.setImagenUrl(null);

        repository.save(producto);
    }

    @Override
    public void eliminar(
            UUID id
    ) {

        Producto producto =
                repository.findById(id)
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Producto no encontrado"
                                )
                        );

        producto.setActivo(false);

        repository.save(producto);
    }
}