*** Settings ***
Resource        resource_api_event.robot
Resource        script_api_event.robot

Test Setup    Criar Sessao API

*** Variables ***
${TAG_ALL_TESTS}    smoke    regression
${TAG_SUCCESS}      success
${TAG_FAILURE}      failure

*** Test Cases ***

# --- CENÁRIOS DE SUCESSO - FLUXO PONTA A PONTA ---

TC-PATCH-001 - Validar atualização de nome do ticket type
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}
    [Documentation]    Fluxo: Evento → Ticket → PATCH (nome) → Validar
    
    # 1. Criar Evento e Ticket Type
    ${event_id}    ${ticket_type_id}=    Criar Evento E Ticket Para Atualizacao
    
    # 2. Executar PATCH para atualizar nome
    ${novo_nome}=    Set Variable    Ingresso VIP Atualizado
    &{payload}=    Criar Payload Atualizacao Parcial    name    ${novo_nome}
    ${response}=    Executar Patch Ticket Type    ${ticket_type_id}    ${payload}    ${200}
    
    # 3. Validações
    Validar Response Ticket Atualizado    ${response}    ${ticket_type_id}    ${event_id}
    Should Be Equal    ${response.json()['name']}    ${novo_nome}

TC-PATCH-002 - Validar atualização de preço do ticket type
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}
    [Documentation]    Fluxo: Evento → Ticket → PATCH (preço) → Validar
    
    # 1. Criar Evento e Ticket Type
    ${event_id}    ${ticket_type_id}=    Criar Evento E Ticket Para Atualizacao
    
    # 2. Executar PATCH para atualizar preço
    &{payload}=    Criar Payload Atualizacao Parcial    price    ${200}
    ${response}=    Executar Patch Ticket Type    ${ticket_type_id}    ${payload}    ${200}
    
    # 3. Validações
    Validar Response Ticket Atualizado    ${response}    ${ticket_type_id}    ${event_id}
    Should Be Equal As Numbers    ${response.json()['price']}    200

TC-PATCH-003 - Validar atualização de capacidade dentro do limite
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}
    [Documentation]    Fluxo: Evento → Ticket → PATCH (capacidade) → Validar
    
    # 1. Criar Evento e Ticket Type
    ${event_id}    ${ticket_type_id}=    Criar Evento E Ticket Para Atualizacao
    
    # 2. Executar PATCH para aumentar capacidade (dentro do limite do evento)
    &{payload}=    Criar Payload Atualizacao Parcial    capacity     2
    ${response}=    Executar Patch Ticket Type    ${ticket_type_id}    ${payload}    ${200}
    
    # 3. Validações
    Validar Response Ticket Atualizado    ${response}    ${ticket_type_id}    ${event_id}
    Should Be Equal As Numbers    ${response.json()['capacity']}    80

# --- CENÁRIOS DE FALHA - REGRAS DE NEGÓCIO PONTA A PONTA ---

TC-PATCH-004 - Validar falha ao atualizar com capacidade inferior aos ingressos já comprados
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}    end-to-end    mercado-pago-api
    [Documentation]    Fluxo COMPLETO: evento → ticket type → pagamento via API
    
    # === EXECUTAR FLUXO COMPLETO ===
    ${response_compra}    ${ticket_type_id}=    Executar Fluxo Completo Pagamento    3
    
    # === VALIDAÇÕES DETALHADAS ===
    Log    \n✅ VALIDAÇÕES DA COMPRA    console=True
    
    # Validar resposta da compra
    Validar_Response_Compra_Sucesso    ${response_compra}
    
    ${response_json}=    Set Variable    ${response_compra.json()}
    
    &{payload}=    Criar Payload Atualizacao Parcial    capacity    2
    Sleep    15s
    ${response}=    Executar Patch Ticket Type    ${ticket_type_id}    ${payload}    ${409}


TC-PATCH-005 - Validar falha ao usar nome duplicado no mesmo evento
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_FAILURE}
    [Documentation]    Fluxo: Evento → 2 Tickets → PATCH (nome duplicado) → Validar falha
    
    # 1. Criar Evento com dois tickets
    ${event_id}    ${ticket_type_1}    ${ticket_type_2}    ${nome_ticket_1}=    Criar Dois Tickets No Mesmo Evento
    
    # 2. Tentar PATCH no segundo ticket com nome do primeiro
    &{payload}=    Criar Payload Atualizacao Parcial    name    ${nome_ticket_1}
    ${response}=    Executar Patch Ticket Type    ${ticket_type_2}    ${payload}    ${409}
    

TC-PATCH-006 - Validar falha ao atualizar ticket type inexistente
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_FAILURE}
    [Documentation]    Fluxo: Tentar PATCH em UUID inexistente → Validar falha 404
    
    # UUID aleatório no formato correto que não existe
    ${uuid_inexistente}=    Set Variable    a1b2c3d4-e5f6-7890-abcd-ef1234567890
    
    # Payload simples apenas com name
    &{payload}=    Create Dictionary    name=Teste Atualizacao
    
    # Executar PATCH
    ${response}=    Executar Patch Ticket Type    ${uuid_inexistente}    ${payload}    ${404}


TC-PATCH-007 - Validar falha ao exceder capacidade total do evento
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_FAILURE}
    [Documentation]    Fluxo: Evento → Ticket → PATCH (capacidade excessiva) → Validar falha
    
    # 1. Criar Evento (capacidade 100) e Ticket
    ${event_id}    ${ticket_type_id}=    Criar Evento E Ticket Para Atualizacao
    
    # 2. Tentar PATCH para capacidade maior que evento (150 > 100)
    &{payload}=    Criar Payload Atualizacao Parcial    capacity    ${150}
    ${response}=    Executar Patch Ticket Type    ${ticket_type_id}    ${payload}    ${409}

TC-PATCH-008 - Validar atualização com datas válidas
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}
    [Documentation]    Fluxo: Evento → Ticket → PATCH (datas válidas) → Validar
    
    # 1. Criar Evento e Ticket Type
    ${event_id}    ${ticket_type_id}=    Criar Evento E Ticket Para Atualizacao
    
    # 2. Executar PATCH com datas dentro do período do evento
    &{payload}=    Create Dictionary
    ...    dateInitial=2030-10-10T12:00:00
    ...    dateFinal=2030-10-10T20:00:00
    ${response}=    Executar Patch Ticket Type    ${ticket_type_id}    ${payload}    ${200}
    
    # 3. Validações
    Validar Response Ticket Atualizado    ${response}    ${ticket_type_id}    ${event_id}

TC-PATCH-009 - Validar falha com datas fora do período do evento
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_FAILURE}
    [Documentation]    Fluxo: Evento → Ticket → PATCH (datas inválidas) → Validar falha
    
    # 1. Criar Evento e Ticket Type
    ${event_id}    ${ticket_type_id}=    Criar Evento E Ticket Para Atualizacao
    
    # 2. Tentar PATCH com data após término do evento
    ${data_invalida}=    Set Variable    2030-10-10T22:00:00  # Após 21:00
    &{payload}=    Criar Payload Atualizacao Parcial    dateFinal    ${data_invalida}
    ${response}=    Executar Patch Ticket Type    ${ticket_type_id}    ${payload}    ${409}

TC-PATCH-010 - Validar atualização múltipla de campos
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}
    [Documentation]    Fluxo: Evento → Ticket → PATCH (múltiplos campos) → Validar
    
    # 1. Criar Evento e Ticket Type
    ${event_id}    ${ticket_type_id}=    Criar Evento E Ticket Para Atualizacao
    
    # 2. Executar PATCH com múltiplos campos
    &{payload}=    Create Dictionary
    ...    name=Ingresso Premium
    ...    description=Descrição atualizada do ingresso
    ...    price=300
    ...    capacity=75
    ${response}=    Executar Patch Ticket Type    ${ticket_type_id}    ${payload}    ${200}
    
    # 3. Validações completas
    Validar Response Ticket Atualizado    ${response}    ${ticket_type_id}    ${event_id}
    Should Be Equal    ${response.json()['name']}        Ingresso Premium
    Should Be Equal    ${response.json()['description']}    Descrição atualizada do ingresso
    Should Be Equal As Numbers    ${response.json()['price']}    300
    Should Be Equal As Numbers    ${response.json()['capacity']}    75